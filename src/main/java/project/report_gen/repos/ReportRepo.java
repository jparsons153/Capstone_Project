package project.report_gen.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.report_gen.models.Report;

@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {
}
