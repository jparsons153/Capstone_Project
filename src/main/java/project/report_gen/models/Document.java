package project.report_gen.models;

import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Document {
    //@Generated
    Long id; // change to Long for DB
    String name;

    @Override
    public String toString() {
        return name +" Document";
    }
//    private String fileName;
//    private String fileType;
//
//    @Lob
//    private byte[] data;
//
//    @Transient
//    private String downloadUrl;
}
