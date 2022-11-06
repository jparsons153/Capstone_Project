package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import project.report_gen.exceptions.NoSuchDocumentException;
import project.report_gen.models.Document;
import project.report_gen.repos.DocumentRepo;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    @Autowired
    final DocumentRepo documentRepo;

    @Transactional
    public List<Document> getAllDocTypes() {
        ArrayList<Document> documentList = new ArrayList<>(documentRepo.findAll());
        return documentList;
    }

    @Transactional
    public Document saveDoc(Document doc) {
        documentRepo.save(doc);
        return doc;
    }

    @Transactional
    public Document getDoc(Long id) throws NoSuchDocumentException {
        Optional<Document> documentOptional = documentRepo.findById(id);

        if (documentOptional.isEmpty()) {
            throw new NoSuchDocumentException("No document with ID " + id + "could not be found");
        }
        return documentOptional.get();
    }

    public void deleteAllDocs() {
        documentRepo.deleteAll();
    }
}