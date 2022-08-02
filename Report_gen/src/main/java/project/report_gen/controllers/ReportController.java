package project.report_gen.controllers;


import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.XPathFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import project.report_gen.models.Report;
import project.report_gen.services.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class ReportController {

    // auto wire service method
    @Autowired
    ReportService reportService;

    // use hello World html template taking user input from Path variable
    @GetMapping("/helloWorld/{name}")
    public String printTemplate(Model model, @PathVariable(name = "name") String nameOne){
        String name = nameOne;

        model.addAttribute("name", name);

        return "helloWorld";
    }

    // add word template and download byte file
    @GetMapping("/doc")
    @ResponseBody
    public void downloadDoc(HttpServletResponse response) {
        String docx = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/TEMPLATE_DOCX.docx";
        WordprocessingMLPackage template;
        try {
            template = WordprocessingMLPackage.load(Files.newInputStream(Paths.get(docx)));
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            template.save(baos);
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().close();
            response.setStatus(HttpStatus.OK.value());
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/mapToReport/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> printObj(@PathVariable String fileName,@RequestBody Report report) throws Exception {
        if(StringUtils.isEmpty(report.getProduct())){
            return ResponseEntity.badRequest().body(report);
        }

        reportService.createReport(report,fileName);

        return ResponseEntity.ok().body(report);
    }

    @GetMapping("/parts/{fileName}")
    public void printParts(@PathVariable String fileName) throws Exception {
        String filePath = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/" + fileName + ".docx";
        reportService.getMainDocumentPart(filePath);
    }

    @GetMapping("/parts/bind")
    public void printParts() throws Exception {
            String filePath = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Docx4J_demo/sample-docs/databinding/binding-simple.docx";
            reportService.getMainDocumentPart(filePath);
        }

    public static JAXBContext context = org.docx4j.jaxb.Context.jc;
    static String filepathprefix;

    @GetMapping("/invoice")
    @ResponseBody
    public void createInvoice() throws Docx4JException, FileNotFoundException {
        // Without Saxon, you are restricted to XPath 1.0
//        boolean USE_SAXON = true; // set this to true; add Saxon to your classpath, and uncomment below

//        String input_DOCX = System.getProperty("user.dir") + "/sample-docs/databinding/invoice.docx";
		String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/invoice_Saxon_XPath2.docx";

        String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/invoice-data.xml";


        // resulting docx
        String OUTPUT_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/OUT_ContentControlsMergeXML.docx";

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());


        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(new File(input_XML));

        // Do the binding:
        // FLAG_NONE means that all the steps of the binding will be done,
        // otherwise you could pass a combination of the following flags:
        // FLAG_BIND_INSERT_XML: inject the passed XML into the document
        // FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope handling)
        // FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the content remains)
        // FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document

        // BindingHyperlinkResolver is used by default
        // BindingHandler.setHyperlinkResolver(new BindingHyperlinkResolverForOpenAPI3());
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        // Defaults to ValueInserterPlainTextImpl()
        // BindingHandler.setValueInserterPlainText(new ValueInserterPlainTextForOpenAPI3());

        //Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);
        //If a document doesn't include the Opendope definitions, eg. the XPathPart,
        //then the only thing you can do is insert the xml
        //the example document binding-simple.docx doesn't have an XPathPart....
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML);// | Docx4J.FLAG_BIND_REMOVE_SDT);

        // That won't invoke any XSLT Finisher (available in 6.1.0).  For that, you need to use signature:
        //  bind(WordprocessingMLPackage wmlPackage, Document xmlDocument, int flags, DocxFetcher docxFetcher,
        //  		XsltProvider xsltProvider, String xsltFinisherfilename, Map<String, Map<String, Object>> finisherParams)
        // Here is an example of using it on invoice.docx to shade a table row:
/*	    // Signature requires Document, not stream
  		Document xmlDoc = XmlUtils.getNewDocumentBuilder().parse(xmlStream);
  		// Specify the finished XSLT we want to us
	    Docx4jProperties.setProperty("docx4j.model.datastorage.XsltFinisher.xslt",  "XsltFinisherInvoice.xslt");
	    // Configure properties
	    Map<String, Map<String, Object>> finisherParams = new HashMap<String, Map<String, Object>>();
		Map<String, Object> tcPrParams = new HashMap<String, Object>();
		tcPrParams.put("fillColor", "5250D0"); // color to use for shading
		finisherParams.put("t_r",  tcPrParams);
		// Now perform the binding
		Docx4J.bind(wordMLPackage, xmlDoc, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML,
				null, new XsltProviderImpl(), null, finisherParams);
*/
        //Save the document
        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + OUTPUT_DOCX);
    }

    @GetMapping("/report")
    @ResponseBody
    public void bindReportToXML() throws Docx4JException, FileNotFoundException {

        String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/document3.docx";

        String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/report-data.xml";

        // resulting docx
        String OUTPUT_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/OUT_ContentControlsMergeXML.docx";

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(new File(input_XML));

        // Do the binding:
        // FLAG_NONE means that all the steps of the binding will be done,
        // otherwise you could pass a combination of the following flags:
        // FLAG_BIND_INSERT_XML: inject the passed XML into the document
        // FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope handling)
        // FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the content remains)
        // FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document

        // BindingHyperlinkResolver is used by default
        // BindingHandler.setHyperlinkResolver(new BindingHyperlinkResolverForOpenAPI3());
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        // Defaults to ValueInserterPlainTextImpl()
        // BindingHandler.setValueInserterPlainText(new ValueInserterPlainTextForOpenAPI3());

        //Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);
        //If a document doesn't include the Opendope definitions, eg. the XPathPart,
        //then the only thing you can do is insert the xml
        //the example document binding-simple.docx doesn't have an XPathPart....
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML);// | Docx4J.FLAG_BIND_REMOVE_SDT);

        // That won't invoke any XSLT Finisher (available in 6.1.0).  For that, you need to use signature:
        //  bind(WordprocessingMLPackage wmlPackage, Document xmlDocument, int flags, DocxFetcher docxFetcher,
        //  		XsltProvider xsltProvider, String xsltFinisherfilename, Map<String, Map<String, Object>> finisherParams)
        // Here is an example of using it on invoice.docx to shade a table row:
/*	    // Signature requires Document, not stream
  		Document xmlDoc = XmlUtils.getNewDocumentBuilder().parse(xmlStream);
  		// Specify the finished XSLT we want to us
	    Docx4jProperties.setProperty("docx4j.model.datastorage.XsltFinisher.xslt",  "XsltFinisherInvoice.xslt");
	    // Configure properties
	    Map<String, Map<String, Object>> finisherParams = new HashMap<String, Map<String, Object>>();
		Map<String, Object> tcPrParams = new HashMap<String, Object>();
		tcPrParams.put("fillColor", "5250D0"); // color to use for shading
		finisherParams.put("t_r",  tcPrParams);
		// Now perform the binding
		Docx4J.bind(wordMLPackage, xmlDoc, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML,
				null, new XsltProviderImpl(), null, finisherParams);
*/

        //Save the document
        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + OUTPUT_DOCX);
    }
}