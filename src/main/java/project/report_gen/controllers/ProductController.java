package project.report_gen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.report_gen.models.Product;
import project.report_gen.services.ProductService;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/newProduct")
    public String showAddProductPage (Model productModel){
        Product product = new Product();
        productModel.addAttribute("product",product);

        return "newProduct";
    }

    @PostMapping("/addProduct")
    public String saveProduct(@ModelAttribute("product") Product product){
        productService.saveProduct(product);
        return "redirect:/new";
    }

}
