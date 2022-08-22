package project.report_gen;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import project.report_gen.models.Report;
import project.report_gen.services.ReportService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ReportGenApplication implements CommandLineRunner {

	@Autowired
	private ReportService reportService;

	public static void main(String[] args) {

		SpringApplication.run(ReportGenApplication.class, args);
	}

	// create a demo Report object
	@Override
	public void run(String... args) throws Exception {
		if (reportService.getAllReports().isEmpty()){
			reportService.saveReport(
					Report.builder()
							.id(1L)
							.documentType("Report")
							.productSKU("41045 Spigot A")
							.tool(205)
							.productionCell("CD")
							.build());
		}
	}
}
