package project.report_gen;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ReportController {

    // auto wire service method

    @GetMapping("/helloWorld")
    public String printMessage(){
        //System.out.println("Hello World from ReportController!");

        return "helloWorld";
    }
}
