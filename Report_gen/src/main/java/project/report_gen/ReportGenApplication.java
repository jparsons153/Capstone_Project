package project.report_gen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import project.report_gen.models.*;
import project.report_gen.services.*;

import java.util.ArrayList;

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

		// delete all documents, val strategy, products & reports
		documentService.deleteAllDocs();
		validationService.deleteAllVals();
		productService.deleteAllProducts();
		reportService.deleteAllReports();

		Document plan = new Document(1L,"Validation Plan");
		Document protocol = new Document(2L,"Protocol");
		Document report = new Document(3L,"Report");

		documentService.saveDoc(plan);
		documentService.saveDoc(protocol);
		documentService.saveDoc(report);


		ValidationStrategy newTool = new ValidationStrategy(1L,"New Tool",6,"General");
		ValidationStrategy duplicateTool = new ValidationStrategy(2L,"Duplicate Tool",7,"Tightened");

		validationService.saveVal(newTool);
		validationService.saveVal(duplicateTool);

		// create Products - Widget & Spinning Wheel using builder & productService
		if(productService.getAllProducts().isEmpty() && reportService.getAllReports().isEmpty()) {

		Defect scratches = Defect.builder().description("Scratches").aql(0.01).build();
		Defect scuffs = Defect.builder().description("Scuffs").aql(1.0).build();
		Defect damage = Defect.builder().description("Damage").aql(0.5).build();

		ArrayList<Defect> widgetDefects = new ArrayList<Defect>();
		widgetDefects.add(scratches);
		widgetDefects.add(scuffs);
		widgetDefects.add(damage);

		Product widget = Product.builder().id(1L).SKU(200345L).name("Widget").minAQL(0.01).batchSize(5000).defectList(widgetDefects).build();
		productService.saveProduct(widget);

		Product spinningWheel = Product.builder().id(2L).SKU(500346L).name("Spinning Wheel").minAQL(0.1).batchSize(45000).defectList(widgetDefects).build();
		productService.saveProduct(spinningWheel);

		reportService.saveReport(
			Report.builder()
					.id(1L)
					.documentType(plan)
					.productSKU(widget)
					.tool(205)
					.productionCell("CD")
					.validationStrategy(duplicateTool)
					.batchSize(5000)
					.build());
		}
	}
}