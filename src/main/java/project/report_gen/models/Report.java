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
@XmlType(propOrder = {"id","documentType", "productSKU", "productionCell","tool", "validationStrategy"})
public class Report {
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Document documentType; // could be enum or list, as is fixed
    private Product productSKU; // use product list dataset
    private int tool;
    private String productionCell; // use productionCell dataset
    private ValidationStrategy validationStrategy; // enum or list

//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();

    // list of defects and product spec derived from product table

    @XmlElement(name = "reportID")
    public void setId(Long id){
        this.id = id;
    }

    @XmlElement(name = "documentType")
    public void setDocumentType(Document documentType){
        this.documentType = documentType;
    }

    @XmlElement(name = "productSKU")
    public void setProductSKU(Product productSKU) {
        this.productSKU = productSKU;
    }

    @XmlElement(name = "tool")
    public void setTool(int tool) {
        this.tool = tool;
    }

    @XmlElement(name = "productCell")
    public void setProductionCell(String productionCell) {
        this.productionCell = productionCell;
    }

    @XmlElement(name = "validationStrategy")
    public void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }
}
