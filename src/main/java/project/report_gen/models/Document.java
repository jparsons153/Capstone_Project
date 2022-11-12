package project.report_gen.models;

import javax.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    @XmlTransient
    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlTransient
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @XmlTransient
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return name;
    }
}