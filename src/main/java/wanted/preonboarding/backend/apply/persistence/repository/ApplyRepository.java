package wanted.preonboarding.backend.apply.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.apply.persistence.entity.Apply;

public interface ApplyRepository extends JpaRepository<Apply, Long> {
}
