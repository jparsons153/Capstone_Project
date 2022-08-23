package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.DocumentType;
import project.report_gen.models.ValidationStrategy;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    List<DocumentType> documentTypeList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<DocumentType> getAllDocTypes() {
        return documentTypeList;
    }

    // Update method to invoke and return repository.save(report)
    public DocumentType saveDoc(DocumentType doc) {
        documentTypeList.add(doc);
        return doc;
    }
}