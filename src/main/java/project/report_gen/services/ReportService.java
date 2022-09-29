package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.XPathFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Document;
import project.report_gen.models.Product;
import project.report_gen.models.Report;
import project.report_gen.models.ValidationStrategy;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    @Autowired
    private ProductService productService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ValidationService validationService;

    List<Report> reportList = new ArrayList<Report>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<Report> getAllReports() {
        return reportList;
    }

    public Report getReport(int id){
        return reportList.get(id);
    }

    // Update method to invoke and return repository.save(report)
    public Report saveReport(Report report) {
        reportList.add(report);
        return report;
    }

    public Boolean deleteAllReports(){return reportList.removeAll(reportList);}

    @Transactional
    public void assignDoc(Report reportAssigned, int documentId, int productID, int validationID){
        Document documentAssigned = documentService.getDoc(documentId);
        reportAssigned.setDocumentType(documentAssigned);

        Product productAssigned = productService.getProduct(productID);
        reportAssigned.setProductSKU(productAssigned);

        ValidationStrategy valAssigned = validationService.getVal(validationID);
        reportAssigned.setValidationStrategy(valAssigned);

        saveReport(reportAssigned);
    }

    public void defectTable(Report report){
        // get report.productSKU.minAQL
        // get validationStrategy.inspectionLevel & validationStrategy.type (normal, tightened or reduced)
        // get sample size based on minAQL, inspectionLevel & type - create arrayList of acc/rej for each AQL based on sampleSize
        // create defect table, write each defect from defectList[] to a new line - col.1 id, col.2 description & col.3 AQL
        // write acc/rej to column in defect table

        System.out.println("Product" + report.getProductSKU().getName() + "min AQL = " + report.getProductSKU().getMinAQL());
        System.out.println("Validation inspection level" + report.getValidationStrategy().getType() + report.getValidationStrategy().getInspectionLevel());

    }

    public void updateReport(Report report, HttpServletResponse response) throws IOException, Docx4JException {
        File inputXML = bindPOJOtoXML(report);
        xmlToDocx(inputXML, response);
    }

    // maps Report obj created in new-document form to XML
    public File bindPOJOtoXML(Report report) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Formats XML
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File
            File file = new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/src/main/resources/inputXML.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(report, file);
            System.out.println("Saved: " + file);

            return file;

        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("JAXB exception - no output XML file available");
            return null;
        }
    }

    // updates template file with XML elements in input file
    // note template file MUST HAVE xml elements already mapped to content controls i.e. Xpath created
    public void xmlToDocx(File input_XML, HttpServletResponse response) throws Docx4JException, IOException {

        String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/src/main/resources/TEMPLATE_DOCX.docx";

        // resulting docx
        // TODO pop-up display box when Document created

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(input_XML);

        // Do the binding, BindingHyperlinkResolver is used by default
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        // TODO check that xml mapping pane in MS Word doesn't display on users m/c
        //complete full binding; inject the xml into the document content controls and delete content controls (sdt) and xml
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE); // | Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT | FLAG_BIND_REMOVE_XML

        //Save the document
//        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
//        System.out.println("Saved: " + OUTPUT_DOCX);

        // save output file as file.docx
        // TODO request param to take inputs from new-document.html to save as ${documentType} + ${id} + ${validation strategy}
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            wordMLPackage.save(baos);
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION,String.format("attachment;filename=file.docx"));
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().close();
            response.setStatus(HttpStatus.OK.value());
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Update method to invoke and return repository.saveAll(reportList)
    public List<Report> saveAllReports(List<Report> list) {
        return list;
    }
}