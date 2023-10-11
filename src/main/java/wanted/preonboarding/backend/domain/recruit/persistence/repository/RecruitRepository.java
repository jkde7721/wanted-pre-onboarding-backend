package wanted.preonboarding.backend.domain.recruit.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

public interface RecruitRepository extends JpaRepository<Recruit, Long>, RecruitRepositoryCustom {
}
