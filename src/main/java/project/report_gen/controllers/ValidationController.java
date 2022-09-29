package project.report_gen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.report_gen.models.ValidationStrategy;
import project.report_gen.services.ValidationService;

@Controller
public class ValidationController {

    @Autowired
    ValidationService validationService;

    @GetMapping("/newVal")
    public String showNewValPage(Model vModel) {
        ValidationStrategy validationStrategy = new ValidationStrategy();
        vModel.addAttribute("validationStrategy",validationStrategy);

        return "newVal";
    }

    @PostMapping("/createVal")
    // creates a new Validation Strategy in DB based on object collected from HTML page
    public String saveVal(@ModelAttribute("validationStrategy") ValidationStrategy validationStrategy){
        validationService.saveVal(validationStrategy);
        return "redirect:/reportIndex";
    }

}
