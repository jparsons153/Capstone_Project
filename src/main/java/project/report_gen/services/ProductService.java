package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.models.Defect;
import project.report_gen.models.Product;

import java.io.*;
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


    public ArrayList<Defect> csvDefects(MultipartFile file) {

        ArrayList<Defect> defects = new ArrayList();
        String line;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){

            reader.readLine();
            while ((line = reader.readLine()) != null) {
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

        return defects;
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
