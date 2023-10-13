package wanted.preonboarding.backend.domain.recruit.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

import java.util.List;
import java.util.Optional;

public interface RecruitRepositoryCustom {

    Page<Recruit> findAllFetch(Pageable pageable);
    Optional<Recruit> findByIdFetch(Long recruitId);
    List<Recruit> findByCompanyNotEqualRecruitOrderByLatest(Long companyId, Long recruitId);
    Page<Recruit> findByQueryFetch(String query, Pageable pageable);
}
