package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    Long SKU;
    String name;
    Double minAQL;
    int batchSize;

    @Override
    public String toString() {
        return  SKU + " " + name;
    }
}
