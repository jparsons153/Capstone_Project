package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Report;

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
    public List<Report> getAllReports(){
        return reportList;
    }

    // Update method to invoke and return repository.save(report)
    // TODO Call method to bind POJO to xml file validationReport-data.xml
    public Report saveReport(Report report){
        reportList.add(report);
        return report;
    }

    // Update method to invoke and return repository.saveAll(reportList)
    public List<Report> saveAllReports(List<Report> list){
        return list;
    }

}
