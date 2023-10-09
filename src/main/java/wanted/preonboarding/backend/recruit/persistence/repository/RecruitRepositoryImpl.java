package wanted.preonboarding.backend.recruit.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.web.dto.response.RecruitListResponse;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Projections.*;
import static wanted.preonboarding.backend.company.persistence.entity.QCompany.*;
import static wanted.preonboarding.backend.recruit.persistence.entity.QRecruit.*;

@RequiredArgsConstructor
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RecruitListResponse> findAllFetch(Pageable pageable) {
        List<RecruitListResponse> recruitList = queryFactory
                .select(constructor(RecruitListResponse.class,
                        recruit.id, company.name, company.nation, company.region,
                        recruit.position, recruit.compensationFee, recruit.skills
                ))
                .from(recruit)
                .join(recruit.company, company)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recruit.createdDate.desc(), recruit.id.desc()) //생성일, 아이디 기준 내림차순 조회 (최신순)
                .fetch();
        Long total = queryFactory.select(recruit.count()).from(recruit).fetchOne();
        return new PageImpl<>(recruitList, pageable, total == null ? 0 : total);
    }

    @Override
    public Optional<Recruit> findByIdFetch(Long recruitId) {
        return Optional.ofNullable(queryFactory.selectFrom(recruit)
                .join(recruit.company).fetchJoin()
                .where(recruit.id.eq(recruitId))
                .fetchOne());
    }
}
