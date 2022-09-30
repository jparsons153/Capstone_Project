package project.report_gen.models;

import lombok.*;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private int id; // change to Long for DB connection
    private Long SKU;
    private String name;
    private int batchSize;
    private ArrayList<Defect> defectList = new ArrayList<Defect>();

    public double getMinAQL(){
        double calcMinAQL = defectList.get(0).getAql();

        for (Defect defect: defectList) {
            if(defect.getAql()<=calcMinAQL){
                calcMinAQL = defect.getAql();
            }
        }
        return calcMinAQL;
    }

    @Override
    public String toString() {
        return  SKU + " " + name;
    }
}
