package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
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

}