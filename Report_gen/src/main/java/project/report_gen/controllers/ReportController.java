package project.report_gen.controllers;



import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.XPathFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.report_gen.models.Report;
import project.report_gen.services.DocCreateService;
import project.report_gen.services.ReportService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    // auto wire service method
    @Autowired
    DocCreateService docCreateService;

    @Autowired
    ReportService reportService;

    // test method to check marshalling of xml
    @GetMapping("/bind")
    @ResponseBody
    public void marshal() throws JAXBException, IOException {

        Report newReport = new Report();
        newReport.setId(3L);
        newReport.setDocumentType("Validation Plan");
        newReport.setProductSKU("Widget");
        newReport.setProductionCell("Cell AB");
        newReport.setTool(1002);

        JAXBContext context = JAXBContext.newInstance(Report.class);
        Marshaller mar= context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        mar.marshal(newReport, new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/reportXML.xml"));
    }

    @GetMapping("/reportIndex")
    public String viewHomePage(Model model) {
        List<Report> reportList = reportService.getAllReports();
        model.addAttribute("reportList", reportList);
    return "reportIndex";
    }

    @GetMapping("/new")
    public String showNewDocumentPage(Model model) {
        // Here a new (empty) Report is created and then sent to the view
        Report report = new Report();
        model.addAttribute("report", report);
        return "new-document";
    }

    @PostMapping(value = "/save")
    // creates a Report in DB based on object collected from HTML page
    // binds Report obj to xml file
    public String saveReport(@ModelAttribute("report") Report report) {
        reportService.saveReport(report);
        reportService.bindPOJOtoXML(report);
        return "redirect:/reportIndex";
    }

    // updates template docx based on input xml file, saves as separate file on output
    @GetMapping("/generate")
    @ResponseBody
    public void bindReportToXML() throws Docx4JException, IOException {

        String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/TEMPLATE_DOCX.docx";

        // test that file path of XML doesn't matter i.e. as long as XML root element and elements carry over it works -- it does!
        //String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/validationReport-data.xml";
        //String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/inputXML.xml";
        String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/Service_ReportXML.xml";

        // resulting docx
        String OUTPUT_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/outputDoc.docx";

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());

        // Load input_template.docx
        // input template needs to have content controls bound to xml (creating xpath references), these xpaths act as placeholders for xml input
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(new File(input_XML));

        // Do the binding
        // BindingHyperlinkResolver is used by default
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        //inject the xml into the document content controls
        Docx4J.bind(wordMLPackage, xmlStream,Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML);
        // delete content controls and xml when done in the output file
        // Docx4J.bind(wordMLPackage, xmlStream,Docx4J.FLAG_BIND_REMOVE_SDT | FLAG_BIND_REMOVE_XML);

        //Save the document
        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + OUTPUT_DOCX);

        //Open output file - not supported by platform!
       // reportService.openFile(new File(OUTPUT_DOCX));
    }

}