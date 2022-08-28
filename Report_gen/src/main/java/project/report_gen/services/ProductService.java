package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Product;
import project.report_gen.models.Report;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    // create product
    // read / find
    // update product
    // delete product

    List<Product> productList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<Product> getAllProducts() {
        return productList;
    }

    // Update method to invoke and return repository.save(report)
    public Product saveProduct(Product product) {
        productList.add(product);
        return product;
    }

    public Boolean deleteAllProducts(){return productList.removeAll(productList);}
}
