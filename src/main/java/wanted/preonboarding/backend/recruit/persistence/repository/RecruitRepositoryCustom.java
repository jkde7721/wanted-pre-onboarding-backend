package wanted.preonboarding.backend.recruit.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.web.dto.response.RecruitListResponse;

import java.util.List;
import java.util.Optional;

public interface RecruitRepositoryCustom {

    Page<RecruitListResponse> findAllFetch(Pageable pageable);
    Optional<Recruit> findByIdFetch(Long recruitId);
    List<Recruit> findByCompanyNotEqualRecruitOrderByLatest(Long companyId, Long recruitId);
    List<Recruit> findByQueryFetch(String query);
}
