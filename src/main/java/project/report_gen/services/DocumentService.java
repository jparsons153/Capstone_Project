package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.models.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    List<Document> documentList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<Document> getAllDocTypes() {
        return documentList;
    }

    // Update method to invoke and return repository.save(report)
    public Document saveDoc(Document doc) {
        documentList.add(doc);
        return doc;
    }

    public Document getDoc(int id){
        return documentList.get(id);
    }

    public Boolean deleteAllDocs(){
       return documentList.removeAll(documentList);
    }

}