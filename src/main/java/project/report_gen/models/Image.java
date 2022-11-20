package project.report_gen.models;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "imageFiles")

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

//    @Transient
    private String filePath;

    @XmlTransient
    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @XmlTransient
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

//    @XmlTransient
//    public void setFilePath(String filePath) {
//        this.filePath = filePath;
//    }
}