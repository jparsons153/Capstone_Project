package project.report_gen.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double aql;

    @Transient
    @JsonIgnore
    private AcceptReject acceptReject;

    public Defect(String description, double aql) {
        this.description = description;
        this.aql = aql;
    }

    public Defect(Long id, String name, String description, double aql) {
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
