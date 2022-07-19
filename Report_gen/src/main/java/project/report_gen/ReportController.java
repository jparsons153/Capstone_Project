package project.report_gen;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    // auto wire service method

    @GetMapping("/helloWorld")
    public String printHello(){

        return "helloWorld";
    }

    @GetMapping("/hello/{name}")
    public String printName(@PathVariable(name = "name") String name){
        return "hello " + name + "- printed from getMapping method";
    }
}