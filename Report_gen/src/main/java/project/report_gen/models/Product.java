package project.report_gen.models;

import lombok.*;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    Long id;
    Long SKU;
    String name;
    Double minAQL;
    int batchSize;
    ArrayList<Defect> defectList = new ArrayList<Defect>();

    @Override
    public String toString() {
        return  SKU + " " + name;
    }
}
