package wanted.preonboarding.backend.recruit.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.preonboarding.backend.recruit.web.dto.response.RecruitListResponse;

public interface RecruitRepositoryCustom {

    Page<RecruitListResponse> findAllFetch(Pageable pageable);
}
