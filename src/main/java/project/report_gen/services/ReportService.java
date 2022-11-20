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
import project.report_gen.exceptions.NoSuchDocumentException;
import project.report_gen.exceptions.NoSuchProductException;
import project.report_gen.exceptions.NoSuchReportException;
import project.report_gen.exceptions.NoSuchValidationException;
import project.report_gen.models.*;
import project.report_gen.repos.ReportRepo;

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

    @Autowired
    final ReportRepo reportRepo;

    @Transactional
    public List<Report> getAllReports() {
        ArrayList<Report> reportList = new ArrayList<>(reportRepo.findAll());
        return reportList;
    }

    @Transactional
    public Report getReport(Long id) throws NoSuchReportException {
        Optional<Report> reportOptional = reportRepo.findById(id);

        if (reportOptional.isEmpty()){
            throw new NoSuchReportException("No report with ID " + id + "could be found");
        }

        Report report = reportOptional.get();
        return report;
    }

    @Transactional
    public Report saveReport(Report report) {
        reportRepo.save(report);
        return report;
    }

    @Transactional
    public void deleteAllReports(){reportRepo.deleteAll();}

    @Transactional
    public void assignDoc(Report reportAssigned, Long documentId, Long productID, Long validationID) throws NoSuchValidationException, NoSuchProductException, NoSuchDocumentException {
        Document documentAssigned = documentService.getDoc(documentId);
        reportAssigned.setDocumentType(documentAssigned);

        Product productAssigned = productService.getProduct(productID);
        reportAssigned.setProductSKU(productAssigned);

        ValidationStrategy valAssigned = validationService.getVal(validationID);
        reportAssigned.setValidationStrategy(valAssigned);

        saveReport(reportAssigned);
    }
}