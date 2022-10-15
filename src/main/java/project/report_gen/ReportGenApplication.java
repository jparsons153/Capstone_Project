package project.report_gen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import project.report_gen.models.*;
import project.report_gen.services.*;

import java.util.*;

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

		Document plan = new Document(0,"Validation Plan");
		Document protocol = new Document(1,"Protocol");
//		Document report = new Document(3L,"Report");
		documentService.saveDoc(plan);
		documentService.saveDoc(protocol);
//		documentService.saveDoc(report);

		AcceptReject zeroOne = new AcceptReject(0,1);
		AcceptReject oneTwo = new AcceptReject(1,2);
		AcceptReject twoThree = new AcceptReject(2,3);
		AcceptReject threeFour = new AcceptReject(3,4);

		Map<Double, AcceptReject> acceptRejectMap_GL2_800pcs_normal = new HashMap<>();
		acceptRejectMap_GL2_800pcs_normal.put(0.015,zeroOne);
		acceptRejectMap_GL2_800pcs_normal.put(0.025,zeroOne);
		acceptRejectMap_GL2_800pcs_normal.put(0.040,zeroOne);
		acceptRejectMap_GL2_800pcs_normal.put(0.065,oneTwo);
		acceptRejectMap_GL2_800pcs_normal.put(0.10,twoThree);
		acceptRejectMap_GL2_800pcs_normal.put(0.15,threeFour);

		TableRow normalGL2_800_row = TableRow.builder()
				.sampleSize(800)
				.acceptRejectHashMap(acceptRejectMap_GL2_800pcs_normal)
				.build();

		ArrayList<TableRow> normalGL2rows = new ArrayList<>();
		normalGL2rows.add(normalGL2_800_row);

		SampleTable normalGeneralLevelIIsampleTable = SampleTable.builder()
				.tableName("GL2")
				.tableRows(normalGL2rows)
				.build();

		ValidationStrategy newTool = new ValidationStrategy(0,"New Tool",6,"Normal",normalGeneralLevelIIsampleTable);
		ValidationStrategy duplicateTool = new ValidationStrategy(1,"Duplicate Tool",7,"Tightened",normalGeneralLevelIIsampleTable);
		validationService.saveVal(newTool);
		validationService.saveVal(duplicateTool);

		// create Products - Widget & Spinning Wheel using builder & productService
		if(productService.getAllProducts().isEmpty() && reportService.getAllReports().isEmpty()) {

		Defect scratches = Defect.builder().description("Scratches").aql(0.015).build();
		Defect scuffs = Defect.builder().description("Scuffs").aql(0.040).build();
		Defect damage = Defect.builder().description("Damage").aql(0.065).build();
		ArrayList<Defect> widgetDefects = new ArrayList<Defect>();
		widgetDefects.add(scratches);
		widgetDefects.add(scuffs);
		widgetDefects.add(damage);

		Image processFlowWidget = Image.builder().id(1L).fileName("Process flowchart").fileType("JPEG")
				.filePath("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/resources/image5.png")
				.build();

		Product widget = Product.builder().id(0).SKU(200345L).name("Widget").productSpec("BS123").batchSize(5000).processMap(processFlowWidget).defectList(widgetDefects).build();
		productService.saveProduct(widget);
		Product spinningWheel = Product.builder().id(1).SKU(500346L).name("Spinning Wheel").productSpec("BS123").batchSize(45000).processMap(processFlowWidget).defectList(widgetDefects).build();
		productService.saveProduct(spinningWheel);

		reportService.saveReport(
			Report.builder()
					.id(1L)
					.documentType(plan)
					.productSKU(widget)
					.tool(205)
					.productionCell("CD")
					.validationStrategy(duplicateTool)
					.build());
		}
	}
}