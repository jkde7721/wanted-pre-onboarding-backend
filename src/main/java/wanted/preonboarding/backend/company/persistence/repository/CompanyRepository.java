package wanted.preonboarding.backend.company.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.company.persistence.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
