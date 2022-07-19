package project.report_gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ReportGenApplication {

	public static void main(String[] args) {

		SpringApplication.run(ReportGenApplication.class, args);

		//System.out.println("Hello World from Bootstrapping class!");
	}

}
