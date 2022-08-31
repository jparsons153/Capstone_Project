package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Defect {
    String description;
    double aql;
    int accept;
    int reject;

    public Defect(String description, double aql) {
        this.description = description;
        this.aql = aql;
    }

    @Override
    public String toString() {
        return  description + aql +
                "%AQL"+
                "acc" + accept + "/" +
                "rej" + reject;
    }
}
