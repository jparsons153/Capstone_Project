package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Defect {
    // long id;
    // String name;
    String description;
    double aql;
    AcceptReject acceptReject;

    public Defect(String description, double aql) {
        this.description = description;
        this.aql = aql;
    }

    @Override
    public String toString() {
        return  description + aql +
                "%AQL"+
                "acc" + acceptReject.accept + "/" +
                "rej" + acceptReject.reject;
    }
}
