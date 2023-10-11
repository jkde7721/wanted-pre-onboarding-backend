package wanted.preonboarding.backend.domain.company.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
