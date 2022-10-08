package project.report_gen.models;

import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Document {
    //@Generated
    int id; // change to Long for DB
    String name;

//    private String fileName;
//
//    @Lob
//    private byte[] data;
//
//    @Transient
//    private String downloadUrl;

    @XmlTransient
    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public void setData(byte[] data) {
//        this.data = data;
//    }
//
//    public void setDownloadUrl(String downloadUrl) {
//        this.downloadUrl = downloadUrl;
//    }

    @Override
    public String toString() {
        return name;
    }
}
