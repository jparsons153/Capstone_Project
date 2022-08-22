package project.report_gen.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
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
