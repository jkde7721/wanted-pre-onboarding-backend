package wanted.preonboarding.backend.domain.recruit.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.recruit.web.dto.response.*;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.Projections.*;
import static wanted.preonboarding.backend.domain.company.persistence.entity.QCompany.company;
import static wanted.preonboarding.backend.domain.recruit.persistence.entity.QRecruit.recruit;

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

    @Override
    public List<Recruit> findByCompanyNotEqualRecruitOrderByLatest(Long companyId, Long recruitId) {
        return queryFactory.selectFrom(recruit)
                .where(recruit.company.id.eq(companyId), recruit.id.ne(recruitId))
                .orderBy(recruit.createdDate.desc(), recruit.id.desc())
                .fetch();
    }

    @Override
    public Page<RecruitListSearchResponse> findByQueryFetch(String query, Pageable pageable) {
        //TODO: 쿼리 최적화 하기 (like '%query%' 쿼리 발생)
        List<RecruitListSearchResponse> recruitList = queryFactory
                .select(constructor(RecruitListSearchResponse.class,
                        recruit.id, company.name, company.nation, company.region,
                        recruit.position, recruit.compensationFee, recruit.skills
                ))
                .from(recruit)
                .join(recruit.company, company)
                .where(recruit.position.containsIgnoreCase(query).or(
                        recruit.skills.containsIgnoreCase(query)).or(
                        company.name.containsIgnoreCase(query)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recruit.createdDate.desc(), recruit.id.desc())
                .fetch();

        Long total = queryFactory
                .select(recruit.count()).from(recruit)
                .join(recruit.company, company)
                .where(recruit.position.containsIgnoreCase(query).or(
                        recruit.skills.containsIgnoreCase(query)).or(
                        company.name.containsIgnoreCase(query)))
                .fetchOne();
        return new PageImpl<>(recruitList, pageable, total == null ? 0 : total);
    }
}