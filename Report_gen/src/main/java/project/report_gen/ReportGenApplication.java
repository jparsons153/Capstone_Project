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
	}
}
