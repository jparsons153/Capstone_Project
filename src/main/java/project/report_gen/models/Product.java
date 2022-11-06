package project.report_gen.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long SKU;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String productSpec;

    @Column(nullable = false)
    private int batchSize;

    @Transient
    @JsonIgnore
    private Image processMap;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Defect> defectList;

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
    public void setId(Long id) {
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
