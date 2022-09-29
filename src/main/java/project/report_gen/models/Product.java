package project.report_gen.models;

import lombok.*;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    int id; // change to Long for DB connection
    Long SKU;
    String name;
    Double minAQL; // change to method
    int batchSize;
    ArrayList<Defect> defectList = new ArrayList<Defect>();

    @Override
    public String toString() {
        return  SKU + " " + name;
    }
}
