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

import static org.docx4j.Docx4J.FLAG_BIND_REMOVE_XML;

@Controller
@RequiredArgsConstructor
public class ReportController {

    // auto wire service method
    @Autowired
    ReportService reportService;

    @GetMapping("/report")
    @ResponseBody
    public void bindReportToXML() throws Docx4JException, FileNotFoundException {

        String input_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/TEMPLATE_DOCX.docx";

        String input_XML = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/validationReport-data.xml";

        // resulting docx
        String OUTPUT_DOCX = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/outputDoc.docx";

        XPathFactoryUtil.setxPathFactory(new net.sf.saxon.xpath.XPathFactoryImpl());

        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(new File(input_XML));

        // Do the binding
        // BindingHyperlinkResolver is used by default
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        //inject the xml into the document content controls, delete content controls and xml when done in the output file
        Docx4J.bind(wordMLPackage, xmlStream,Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_REMOVE_SDT | FLAG_BIND_REMOVE_XML); // | Docx4J.FLAG_BIND_BIND_XML)

        //Save the document
        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
        System.out.println("Saved: " + OUTPUT_DOCX);
    }
}