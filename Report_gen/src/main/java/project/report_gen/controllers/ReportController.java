package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.DocumentSettingsPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.CTCompat;
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

    @GetMapping("/newDoc")
    public void createNewDoc() {
        try {
            WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();

            // create new paragraph with text "hello World" and add to main document part
            MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
            mainDocumentPart.addParagraphOfText("hello World");

            DocumentSettingsPart dsp = mainDocumentPart.getDocumentSettingsPart(true);
            CTCompat compat = Context.getWmlObjectFactory().createCTCompat();
            dsp.getContents().setCompat(compat);
            compat.setCompatSetting("compatibilityMode", "http://schemas.microsoft.com/office/word", "15");

            wordprocessingMLPackage.save(new FileOutputStream(new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/helloWorld.docx")));

        } catch (Docx4JException | FileNotFoundException exc) {
            throw new RuntimeException(exc);
        }
    }

    @PostMapping(value = "/print/POJOtoReport", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> printObj(@RequestBody Report report){
        if(StringUtils.isEmpty(report.getProduct())){
            return ResponseEntity.badRequest().body(report);
        }
        final Report savedReport = Report.builder().documentType(report.getDocumentType()).product(report.getProduct()).productionCell(report.getProductionCell()).build();

        return ResponseEntity.ok().body(savedReport);
    }

    //TODO
    // add text based on path variable
    // add text using body using Post in Postman
    // what fields are needed in report

    @GetMapping("/parts")
    public void printParts(String doc) throws Exception {
        reportService.getMainDocumentPart(doc);
    }

}