package project.report_gen.models;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String documentType; // could be enum or list, as is fixed
    private String productFamily;
    private String productSKU; // use product list dataset
    private int tool;
    private String productionCell; // use productionCell dataset
    private String validationStrategy; // enum or list
    private int batchSize; // auto-populated based on product SKU

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    // list of defects and product spec derived from product table



}
