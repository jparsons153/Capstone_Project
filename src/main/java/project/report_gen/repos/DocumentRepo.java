package project.report_gen.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.report_gen.models.Document;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
}
