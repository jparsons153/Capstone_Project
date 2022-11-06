package project.report_gen.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import javax.xml.bind.annotation.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

@XmlRootElement(name = "validation_report")
@XmlType(propOrder = {"id","documentType", "productSKU", "productionCell","tool", "validationStrategy", "valSampleSize"})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Document documentType;

    @OneToOne
    private Product productSKU;

    @Column(nullable = false)
    private int tool;

    @Column(nullable = false)
    private String productionCell;

    @OneToOne
    private ValidationStrategy validationStrategy;

    @Column(nullable = false)
    private int valSampleSize;

//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();

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

    @XmlElement(name = "valSampleSize")
    public void setValSampleSize(int valSampleSize) {
        this.valSampleSize = valSampleSize;
    }

    @XmlElement(name = "validationStrategy")
    public void setValidationStrategy(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }
}
