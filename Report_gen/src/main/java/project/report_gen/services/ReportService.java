package project.report_gen.services;

import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ReportService {

    //TODO
    // clean-up methods in Report Controller and move to Service method

    public void getMainDocumentPart(String docx) throws Exception {
        WordprocessingMLPackage template;
        try {
            template = WordprocessingMLPackage.load(Files.newInputStream(Paths.get(docx)));
        } catch (Docx4JException | IOException e) {
            throw new RuntimeException(e);
        }

        MainDocumentPart documentPart = template.getMainDocumentPart();

        // Remove gumpf so it is easier to read
        // Pretty print the main document part
        VariablePrepare.prepare(template);
        System.out.println(
                XmlUtils.marshaltoString(documentPart.getJaxbElement(), true, true) );
    }
}