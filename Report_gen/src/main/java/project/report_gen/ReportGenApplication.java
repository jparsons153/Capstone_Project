package project.report_gen;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ReportGenApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReportGenApplication.class, args);

		String name = "C:\\Users\\User\\OneDrive\\Documents\\CodingNomads\\projects\\Capstone_Project\\report_gen\\src\\main\\java\\project\\report_gen\\VALIDATION_TEMPLATE.dotx";

//		public WordprocessingMLPackage getTemplate(String name) throws Docx4JException, FileNotFoundException {
//			WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
//			return template;
//		}

	}

}
