package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Defect {
    private int id;
    private String name;
    private String description;
    private double aql;
    private AcceptReject acceptReject;

    public Defect(String description, double aql) {
        this.description = description;
        this.aql = aql;
    }

    public Defect(int id, String name, String description, double aql) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.aql = aql;
    }

    @Override
    public String toString() {
        return "Defect{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", aql=" + aql +
                '}';
    }
}
