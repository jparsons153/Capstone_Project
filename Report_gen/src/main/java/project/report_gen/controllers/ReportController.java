package project.report_gen.controllers;


import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.XPathFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.report_gen.models.Product;
import project.report_gen.models.Report;
import project.report_gen.services.DocCreateService;
import project.report_gen.services.ProductService;
import project.report_gen.services.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

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
    @PostMapping(value = "/update")
    public String update(@ModelAttribute("report") Report report, HttpServletResponse response, Model model) throws IOException, Docx4JException {
        reportService.saveReport(report);
        reportService.updateReport(report, response);
    return "redirect:/reportIndex";
    }


}