package wanted.preonboarding.backend.recruit.persistence.repository;

import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;

import java.util.List;

public interface RecruitRepositoryCustom {

    List<Recruit> findAllFetch();
}
