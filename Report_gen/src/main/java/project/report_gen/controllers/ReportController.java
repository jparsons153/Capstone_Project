package project.report_gen.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//RestController

//Only Controller annotation will resolve helloWorld.html template...?
@Controller
public class ReportController {

    // auto wire service method

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


}