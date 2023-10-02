package wanted.preonboarding.backend.recruit.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
}
