package project.report_gen.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "validations")
@Getter
@Builder

public class ValidationStrategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int inspectionLevel; // S1=1, S2=2, S3=3, S4=4, GL1=5, GL2=6, GL3=7

    @Column(nullable = false)
    private String type;

    @Transient
    @JsonIgnore
    private SampleTable sampleTable;
    //Boolean featured;

    @XmlTransient
    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    // TODO create custom method to return toString (e.g. 6 = GL2)
    public void setInspectionLevel(int inspectionLevel) {
        this.inspectionLevel = inspectionLevel;
    }

    @XmlElement
    public void setType(String type) {
        this.type = type;
    }

    @XmlElement
    public void setSampleTable(SampleTable sampleTable) {
        this.sampleTable = sampleTable;
    }
}
