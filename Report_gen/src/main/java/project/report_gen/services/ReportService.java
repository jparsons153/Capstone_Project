package project.report_gen.services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Report;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    // TODO how to map to xml to update validationReport-data.mxl?

    List<Report> reportList = new ArrayList<Report>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<Report> getAllReports() {
        return reportList;
    }

    // Update method to invoke and return repository.save(report)
    // TODO Call method to bind POJO to xml file validationReport-data.xml
    public Report saveReport(Report report) {
        reportList.add(report);
        return report;
    }

    // bind POJO to xml
    // take report POJO from "/save" as input
    // create XmlRootElement "validation report"
    // write to file validationReport-data.xml
    public void bindPOJOtoXML(Report report) {
        jaxbObjectToXML(report);
    }

    private static void jaxbObjectToXML(Report report) {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Store XML to File
            File file = new File("C:/Users/User/OneDrive/Documents/CodingNomads/projects/Capstone_Project/report_gen/src/main/java/project/report_gen/report.xml");

            //Writes XML file to file-system
            jaxbMarshaller.marshal(report, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // Update method to invoke and return repository.saveAll(reportList)
    public List<Report> saveAllReports(List<Report> list) {
        return list;
    }
}