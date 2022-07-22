package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.report_gen.services.ReportService;

import java.io.FileNotFoundException;

//RestController

//Only Controller annotation will resolve helloWorld.html template...?
@Controller
@RequiredArgsConstructor
public class ReportController {

    // auto wire service method
    @Autowired
    ReportService reportService;

    @GetMapping("/helloWorld")
    public String printHello(){

        return "helloWorld";
    }

    @GetMapping("/helloWorld/{name}")
    public String printTemplate(Model model, @PathVariable(name = "name") String nameOne){
        String name = nameOne;

        model.addAttribute("name", name);

        return "helloWorld";
    }

    // add word template file
    String temp = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/VALIDATION_TEMPLATE.dotx";
    String template = "\"src/main/java/project/report_gen/char_data.txt";

    @GetMapping("/template")
    public WordprocessingMLPackage returnTemplate(String temp) throws FileNotFoundException, Docx4JException {
        return reportService.getTemplate(temp);
    }

    // Get method to return input stream of char_data.txt



}