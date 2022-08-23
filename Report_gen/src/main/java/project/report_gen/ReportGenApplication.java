package project.report_gen;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import project.report_gen.models.DocumentType;
import project.report_gen.models.Product;
import project.report_gen.models.Report;
import project.report_gen.models.ValidationStrategy;
import project.report_gen.services.DocumentService;
import project.report_gen.services.ProductService;
import project.report_gen.services.ReportService;
import project.report_gen.services.ValidationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ReportGenApplication implements CommandLineRunner {

	@Autowired
	private ReportService reportService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ValidationService validationService;
	@Autowired
	private DocumentService documentService;

	public static void main(String[] args) {

		SpringApplication.run(ReportGenApplication.class, args);
	}

	// create a demo Report object
	@Override
	public void run(String... args) throws Exception {

		if(documentService.getAllDocTypes().isEmpty()){
			DocumentType plan = new DocumentType(1L,"Validation Plan");
			DocumentType protocol = new DocumentType(2L,"Protocol");
			DocumentType report = new DocumentType(3L,"Report");

			documentService.saveDoc(plan);
			documentService.saveDoc(protocol);
			documentService.saveDoc(report);
		}

		if (validationService.getAllVals().isEmpty()){
			ValidationStrategy newTool = new ValidationStrategy(1L,"New Tool");
			ValidationStrategy duplicateTool = new ValidationStrategy(2L,"Duplicate Tool");

			validationService.saveVal(newTool);
			validationService.saveVal(duplicateTool);
		}

		// create Products - Widget & Spinning Wheel using builder & productService
		if(productService.getAllProducts().isEmpty() && reportService.getAllReports().isEmpty()) {

			Product widget = Product.builder().name("Widget").SKU(200345L).minAQL(0.01).batchSize(5000).build();
			productService.saveProduct(widget);

			Product spinningWheel = Product.builder().name("Spinning Wheel").SKU(101278L).minAQL(1.0).batchSize(40000).build();
			productService.saveProduct(spinningWheel);

			reportService.saveReport(
					Report.builder()
							.id(1L)
							.documentType("Report")
							.productSKU(widget)
							.tool(205)
							.productionCell("CD")
							.build());

			reportService.saveReport(
					Report.builder()
							.id(2L)
							.documentType("Validation Plan")
							.productSKU(spinningWheel)
							.tool(411)
							.productionCell("AB")
							.build());
		}
	}
}
