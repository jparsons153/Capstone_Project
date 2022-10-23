package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Defect;
import project.report_gen.models.Product;
import project.report_gen.models.Report;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public Product getProduct(int id){
        return productList.get(id);
    }

    public Boolean deleteAllProducts(){return productList.removeAll(productList);}


    public static void csvDefects() {

        ArrayList<Defect> defects = new ArrayList();

        String filePath = "C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/src/main/resources/defects.csv";

        try (BufferedReader br =
                     new BufferedReader(new FileReader(filePath))) {

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                defects.add(mapValuesToDefectObject(values));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(Defect defect : defects){
            System.out.println(defect.toString());
        }
    }

    private static Defect mapValuesToDefectObject(String[] values) {

        Defect defect = new Defect();

        defect.setId(Integer.parseInt(values[0]));
        defect.setName(values[1]);
        defect.setDescription(values[2]);
        defect.setAql(Double.parseDouble(values[3]));

        return defect;
    }
}
