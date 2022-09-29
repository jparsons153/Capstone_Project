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
    private Document documentType; // could be enum or list, as is fixed
//    private String productFamily;
    private Product productSKU; // use product list dataset
    private int tool;
    private String productionCell; // use productionCell dataset
    private ValidationStrategy validationStrategy; // enum or list
//    private int batchSize; // auto-populated based on product SKU

//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();

    // list of defects and product spec derived from product table

    @XmlTransient
    public void setId(Long id){
        this.id = id;
    }

    @XmlElement(name = "documentType")
    public void setDocumentType(Document documentType){
        this.documentType = documentType;
    }

//    @XmlElement
//    public void setProductFamily(String productFamily){
//        this.productFamily = productFamily;
//    }

    @XmlElement(name = "productSKU")
    public void setProductSKU(Product productSKU) {
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
    public void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

//    @XmlTransient
//    public void setBatchSize(int batchSize) {
//        this.batchSize = batchSize;
//    }
}
