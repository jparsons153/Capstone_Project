package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.models.*;
import project.report_gen.services.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ReportController {

    // auto wire service method
    @Autowired
    DocCreateService docCreateService;
    @Autowired
    ReportService reportService;
    @Autowired
    ProductService productService;
    @Autowired
    ValidationService validationService;
    @Autowired
    DocumentService documentService;

    // test method to check marshalling of xml
//    @GetMapping("/bind")
//    @ResponseBody
//    public void marshal() throws JAXBException, IOException {
//
//        Report newReport = new Report();
//        newReport.setId(3L);
//        newReport.setDocumentType("Validation Plan");
//        newReport.setProductSKU("Widget");
//        newReport.setProductionCell("Cell AB");
//        newReport.setTool(1002);
//
//        JAXBContext context = JAXBContext.newInstance(Report.class);
//        Marshaller mar= context.createMarshaller();
//        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//        mar.marshal(newReport, new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/reportXML.xml"));
//    }

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

        final List<Product>productList = productService.getAllProducts();
        model.addAttribute("productList",productList);

        final List<ValidationStrategy>valList = validationService.getAllVals();
        model.addAttribute("valList",valList);

        final List<Document> docTypeList = documentService.getAllDocTypes();
        model.addAttribute("docTypeList",docTypeList);

        return "new-document";
    }

    @PostMapping(value = "/save")
    // creates a Report in DB based on object collected from HTML page
    public String saveReport(@ModelAttribute("report") Report report) throws IOException, Docx4JException {
        reportService.saveReport(report);
        return "redirect:/reportIndex";
    }

  //   update template file with input from user form
  // TODO can't call redirect after the response has been committed?
  // TODO move @RequestParam's to hashmap
    @PostMapping(value = "/update")
    public void update(@RequestParam("productID")int productID, @RequestParam("documentID")int documentID,@RequestParam("validationStrategyID")int validationStrategyID,@ModelAttribute("report") Report report, HttpServletResponse response, Model model) throws IOException, Docx4JException {
       reportService.assignDoc(report,documentID, productID, validationStrategyID);
        System.out.println("val strat" + validationStrategyID);
       reportService.defectTable(report);
//       reportService.updateReport(report, response);
    }

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