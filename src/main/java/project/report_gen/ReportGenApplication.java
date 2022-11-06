package project.report_gen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import project.report_gen.models.*;
import project.report_gen.repos.ValidationRepo;
import project.report_gen.services.*;

import java.util.*;

@SpringBootApplication
public class ReportGenApplication implements CommandLineRunner {

	@Autowired
	private ReportService reportService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ValidationService validationService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private ValidationRepo validationRepo;

	public static void main(String[] args) {

		SpringApplication.run(ReportGenApplication.class, args);
	}

	// create sample table
	@Override
	public void run(String... args) throws Exception {

		AcceptReject zeroOne = new AcceptReject(0, 1);
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

		ValidationStrategy newTool = new ValidationStrategy(1L,"New Tool",6,"Normal",normalGeneralLevelIIsampleTable);
		ValidationStrategy duplicateTool = new ValidationStrategy(2L,"Duplicate Tool",7,"Tightened",normalGeneralLevelIIsampleTable);
		validationService.saveVal(newTool);
		validationService.saveVal(duplicateTool);
	}
}