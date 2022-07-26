package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.report_gen.services.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    @GetMapping("/doc")
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


    // Get method to return input stream of char_data.txt
    @GetMapping("/char")
    public static void readLineByLine() throws IOException {
        String anotherFilepath = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/char_data.txt";
        File file = new File(anotherFilepath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null)
            System.out.println(st);
    }

}