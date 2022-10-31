package project.report_gen.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.report_gen.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
