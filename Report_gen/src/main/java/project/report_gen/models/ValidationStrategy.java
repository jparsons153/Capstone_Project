package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidationStrategy {
    //@Generated
    Long id;
    String name;
    int inspectionLevel; // S1=1, S2=2, S3=3, S4=4, GL1=5, GL2=6, GL3=7
    String type;
    Boolean featured;

}
