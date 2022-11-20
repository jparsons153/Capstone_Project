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

		ValidationStrategy newTool = new ValidationStrategy("New Tool",6,"Normal");
		validationService.saveVal(newTool);
	}


}