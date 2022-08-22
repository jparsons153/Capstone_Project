package project.report_gen.models;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.*;

//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

@XmlRootElement(name = "validation_report")
@XmlType(propOrder = {"documentType", "productSKU", "productionCell","tool"})
public class Report {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String documentType; // could be enum or list, as is fixed
//    private String productFamily;
    private String productSKU; // use product list dataset
    private int tool;
    private String productionCell; // use productionCell dataset
    private String validationStrategy; // enum or list
    private int batchSize; // auto-populated based on product SKU

//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();

    // list of defects and product spec derived from product table

    @XmlTransient
    public void setId(Long id){
        this.id = id;
    }

    @XmlElement(name = "documentType")
    public void setDocumentType(String documentType){
        this.documentType = documentType;
    }

//    @XmlElement
//    public void setProductFamily(String productFamily){
//        this.productFamily = productFamily;
//    }

    @XmlElement(name = "product")
    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    @XmlElement
    public void setTool(int tool) {
        this.tool = tool;
    }

    @XmlElement(name = "productCell")
    public void setProductionCell(String productionCell) {
        this.productionCell = productionCell;
    }

    @XmlTransient
    public void setValidationStrategy(String validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    @XmlTransient
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
