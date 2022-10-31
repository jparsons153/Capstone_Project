package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.utils.XPathFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.exceptions.NoSuchProductException;
import project.report_gen.exceptions.NoSuchValidationException;
import project.report_gen.models.*;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    @Autowired
    private ProductService productService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private GenerateDocument generateDocument;

    List<Report> reportList = new ArrayList<Report>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<Report> getAllReports() {
        return reportList;
    }

    public Report getReport(int id){
        return reportList.get(id);
    }

    // Update method to invoke and return repository.save(report)
    public Report saveReport(Report report) {
        reportList.add(report);
        return report;
    }

    public Boolean deleteAllReports(){return reportList.removeAll(reportList);}

    @Transactional
    public void assignDoc(Report reportAssigned, int documentId, Long productID, long validationID) throws NoSuchValidationException, NoSuchProductException {
        Document documentAssigned = documentService.getDoc(documentId);
        reportAssigned.setDocumentType(documentAssigned);

        Product productAssigned = productService.getProduct(productID);
        reportAssigned.setProductSKU(productAssigned);

        ValidationStrategy valAssigned = validationService.getVal(validationID);
        reportAssigned.setValidationStrategy(valAssigned);

        saveReport(reportAssigned);
    }

    // Update method to invoke and return repository.saveAll(reportList)
    public List<Report> saveAllReports(List<Report> list) {
        return list;
    }

}