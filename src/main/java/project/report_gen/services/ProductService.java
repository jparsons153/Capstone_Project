package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.exceptions.NoSuchProductException;
import project.report_gen.models.Defect;
import project.report_gen.models.Image;
import project.report_gen.models.Product;
import project.report_gen.repos.ProductRepo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    @Autowired
    final ProductRepo productRepo;

    @Transactional
    public Product getProduct(Long id) throws NoSuchProductException {
        Optional<Product> productOptional = productRepo.findById(id);

        if (productOptional.isEmpty()){
            throw new NoSuchProductException("No product with ID " + id + "could be found");
        }

        Product product = productOptional.get();
        return product;
    }

    @Transactional
    public List<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>(productRepo.findAll());

        return productList;
    }

    @Transactional
    public Product saveProduct(Product product,MultipartFile file,MultipartFile image) throws IOException {
        productRepo.save(product);
        product.setDefectList(csvDefects(file));
        product.setProcessMap(uploadFile(image));
        productRepo.save(product);
        return product;
    }

    @Transactional
    public void deleteAllProducts(){
        productRepo.deleteAll();
    }


    public ArrayList<Defect> csvDefects(MultipartFile file) throws IOException {

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

        // delete csv once mapped and return defect list
        return defects;
    }

    private static Defect mapValuesToDefectObject(String[] values) {

        Defect defect = new Defect();

        defect.setId(Long.parseLong(values[0]));
        defect.setName(values[1]);
        defect.setDescription(values[2]);
        defect.setAql(Double.parseDouble(values[3]));

        return defect;
    }

    private Image uploadFile(MultipartFile inputFile) {

        String fileName;
        // get the original file name
        if (inputFile == null) {
            throw new IllegalStateException("Sorry did not receive a file, please try again!");
        } else {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(inputFile.getOriginalFilename()));
        }

        Image imageFile = null;
        try {
            imageFile = Image.builder()
                    .data(inputFile.getBytes())
                    .fileName(fileName)
                    .fileType(inputFile.getContentType())
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }

}
