package project.report_gen.models;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {
    private int id; // change to Long for DB connection
    private Long SKU;
    private String name;
    private String productSpec;
    private int batchSize;
    private Image processMap;

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

    @XmlTransient
    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public void setSKU(Long SKU) {
        this.SKU = SKU;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public void setProductSpec(String productSpec) {
        this.productSpec = productSpec;
    }

    @XmlElement
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @XmlElement
    public void setProcessMap(Image processMap) {
        this.processMap = processMap;
    }

    @XmlTransient
    public void setDefectList(ArrayList<Defect> defectList) {
        this.defectList = defectList;
    }

    @Override
    public String toString() {
        return  SKU + " " + name;
    }
}
