package project.report_gen.services;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class ReportService {

    public WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }
}
