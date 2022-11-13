package project.report_gen.services;

import org.docx4j.Docx4J;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.XPathFactoryUtil;
import org.docx4j.wml.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.report_gen.models.Defect;
import project.report_gen.models.Image;
import project.report_gen.models.Report;
import project.report_gen.models.TableRow;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenerateDocument {

    public void defectTable(Report report){
        // get report.productSKU.minAQL
        // get validationStrategy.inspectionLevel & validationStrategy.type (normal, tightened or reduced)
        // get sample size based on minAQL, inspectionLevel & type - create arrayList of acc/rej for each AQL based on sampleSize
        // create defect table, write each defect from defectList[] to a new line - col.1 id, col.2 description & col.3 AQL
        // write acc/rej to column in defect table

        System.out.println("Product " + report.getProductSKU().getName() + " min AQL = " + report.getProductSKU().getMinAQL());
        System.out.println("Validation inspection level " + report.getValidationStrategy().getType() + report.getValidationStrategy().getInspectionLevel());
        System.out.println("Sample table " + report.getValidationStrategy().getSampleTable());
        TableRow tableRowForSample = getSampling(report);
        report.setValSampleSize(tableRowForSample.getSampleSize());
        getDefectAccRej(report,tableRowForSample);
    }

    public TableRow getSampling(Report report) {
        Double productMinAQL = report.getProductSKU().getMinAQL();
        List<TableRow> rowsToIterate = report.getValidationStrategy().getSampleTable().getTableRows();
        TableRow selectRowForSampleSize;

        // check if productMinAQL is a value in map for each row
        // iterate through each row in sampleTable
        // if productMinAQL is equal is key in map, return TableRow for selected sample size

        for (TableRow row: rowsToIterate) {
            if (row.getAcceptRejectHashMap().containsKey(productMinAQL)){
                selectRowForSampleSize = row;
                System.out.println("Sample size required " + selectRowForSampleSize.getSampleSize());
                return selectRowForSampleSize;
            }
            else System.out.println("Sample size could not be found");
        }
        return null;
    }

    public void getDefectAccRej(Report report, TableRow selectRowForSampleSize){

        System.out.println("Updating defect acc / rej to align with validation sampling plan");
        report.getProductSKU().getDefectList().sort((a,b)-> {
            int compare = Double.compare(a.getAql(),b.getAql());
            if (compare==0){
                return a.getDescription().compareTo(b.getDescription());
            }
            return compare;
        });
        for (Defect defect:report.getProductSKU().getDefectList()) {
            if (selectRowForSampleSize.getAcceptRejectHashMap().containsKey(defect.getAql())){
                defect.setAcceptReject(selectRowForSampleSize.getAcceptRejectHashMap().get(defect.getAql()));
                System.out.println(defect.getDescription() + " AQL " + defect.getAql() + "%"
                        + " acc" + defect.getAcceptReject().getAccept() + "/rej" + defect.getAcceptReject().getReject());
            }
            else
                continue;
        }
        System.out.println("Defect acc/rej updated based on selected sampling plan");
    }

    // creating a table here, another option could be to find the first table in document (Table table =
    //                doc.MainDocumentPart.Document.Body.Elements<Table>().First())
    public void addCustomTable(WordprocessingMLPackage wordprocessingMLPackage, MainDocumentPart mainDocumentPart, Report report) {
        int writableWidthTwips = wordprocessingMLPackage.getDocumentModel().getSections().get(0).getPageDimensions().getWritableWidthTwips();
        int qtyCols = 5;
        int qtyRows = report.getProductSKU().getDefectList().size()+1;
        int cellWidthTwips = new Double(Math.floor((writableWidthTwips / qtyCols))).intValue();

        ObjectFactory factory = Context.getWmlObjectFactory();
        Tbl tbl = TblFactory.createTable(qtyRows, qtyCols, cellWidthTwips);
        List<Object> tableRows = tbl.getContent();
        int defectId=0;

        // set table header row
        Tr tr = (Tr) tableRows.get(0);
        List<Object>  cells = tr.getContent();
        ((Tc)cells.get(0)).getContent().add(createCell(factory,"DEFECT #"));
        ((Tc)cells.get(1)).getContent().add(createCell(factory,"DESCRIPTION"));
        ((Tc)cells.get(2)).getContent().add(createCell(factory,"AQL %"));
        ((Tc)cells.get(3)).getContent().add(createCell(factory,"SAMPLE SIZE"));
        ((Tc)cells.get(4)).getContent().add(createCell(factory,"ACC / REJ"));

        for(int j=1; j<tableRows.size(); j++){
            tr = (Tr) tableRows.get(j);
            cells = tr.getContent();

            ((Tc)cells.get(0)).getContent().add(createCell(factory,String.valueOf(defectId +1)));
            ((Tc)cells.get(1)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getDescription())));
            ((Tc)cells.get(2)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getAql())));
            ((Tc)cells.get(3)).getContent().add(createCell(factory,String.valueOf(report.getValSampleSize())));
            ((Tc)cells.get(4)).getContent().add(createCell(factory,String.valueOf(report.getProductSKU().getDefectList().get(defectId).getAcceptReject().toString())));

            defectId++;
        }
        mainDocumentPart.addObject(tbl);
    }

    private static P createCell(ObjectFactory factory, String value){

        P p = factory.createP();
        R r = factory.createR();
        Text t = factory.createText();

        t.setValue(value);

        r.getContent().add(t);
        p.getContent().add(r);

        return p;
    }

    public void addImage(WordprocessingMLPackage wordprocessingMLPackage, Image imageToInsert) throws Exception {

        String imageFilePath = imageToInsert.getFilePath();
        File file = new File(imageFilePath);

        InputStream is = new FileInputStream(file);
        long length = file.length();
        // create an array using int type
        if (length > Integer.MAX_VALUE) {
            System.out.println("File too large!!");
        }

        byte[] bytes = new byte[(int)length];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            System.out.println("Could not completely read file "+imageFilePath);
        }
        is.close();

        String filenameHint = null;
        String altText = null;
        int id2 = 1;

        // Image 2: width 3000pixels
        org.docx4j.wml.P p2 = newImage(wordprocessingMLPackage, bytes,
                filenameHint, altText,
                id2, 9000 );
        wordprocessingMLPackage.getMainDocumentPart().addObject(p2);
    }

    public static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage,
                                             byte[] bytes,
                                             String filenameHint, String altText,
                                             int id2, long cx) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                wordMLPackage.getDrawingPropsIdTracker().generateId(), id2, cx, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.P  p = factory.createP();
        org.docx4j.wml.R  run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        return p;
    }

    public void updateReport(Report report, HttpServletResponse response) throws Exception {
        File inputXML = bindPOJOtoXML(report);
        xmlToDocx(inputXML, response, report);
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
            File file = new File("inputXML.xml");

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
    public void xmlToDocx(File input_XML, HttpServletResponse response,Report report) throws Exception {

        // TODO update for file object
        String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/src/main/resources/TEMPLATE_DOCX.docx";

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // set title as document property
        wordMLPackage.setTitle(report.getDocumentType().getName() + " " + report.getId() + " " + report.getProductSKU().getName() + " in " + report.getProductionCell());

        MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

        documentPart.addParagraphOfText("Programmatic table added");
        addCustomTable(wordMLPackage,documentPart,report);

        documentPart.addParagraphOfText("Added image from file location");
        Image processMap = report.getProductSKU().getProcessMap();
        addImage(wordMLPackage, processMap);

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(input_XML);

        // Do the binding, BindingHyperlinkResolver is used by default
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        //complete full binding; inject the xml into the document content controls and delete content controls (sdt) and xml
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE); // | Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML | Docx4J.FLAG_BIND_REMOVE_SDT | FLAG_BIND_REMOVE_XML

        // return file as downloaded .docx
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            wordMLPackage.save(baos);
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            //response.addHeader(HttpHeaders.CONTENT_DISPOSITION,String.format("attachment;filename=file.docx"));
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION,String.format("attachment; filename=" +wordMLPackage.getTitle() +".docx"));
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().close();
            response.setStatus(HttpStatus.OK.value());
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}