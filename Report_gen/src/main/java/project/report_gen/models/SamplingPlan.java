package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class SamplingPlan {
    //@Generated
    Long id;
    String name;

    @Override
    public String toString() {
        return name;
    }
}
