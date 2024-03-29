package project.report_gen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.exceptions.NoSuchProductException;
import project.report_gen.models.Product;
import project.report_gen.services.ProductService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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

    @PostMapping(value = "/addProduct",consumes = MediaType.ALL_VALUE)
    public String saveProduct(@RequestPart("file") MultipartFile file, @RequestPart("image") MultipartFile image, @ModelAttribute("product") Product product) throws IOException, NoSuchProductException {

        productService.saveProduct(product,file,image);

        return "redirect:/new";
    }
}
