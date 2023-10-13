package wanted.preonboarding.backend.domain.recruit.persistence.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

import java.util.List;
import java.util.Optional;

import static wanted.preonboarding.backend.domain.company.persistence.entity.QCompany.company;
import static wanted.preonboarding.backend.domain.recruit.persistence.entity.QRecruit.recruit;

@RequiredArgsConstructor
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Recruit> findAllFetch(Pageable pageable) {
        List<Recruit> recruitList = queryFactory.selectFrom(recruit)
                .join(recruit.company, company)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(latestOrder())
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
                .orderBy(latestOrder())
                .fetch();
    }

    @Override
    public Page<Recruit> findByQueryFetch(String query, Pageable pageable) {
        //TODO: 쿼리 최적화 하기 (like '%query%' 쿼리 발생)
        List<Recruit> recruitList = queryFactory.selectFrom(recruit)
                .join(recruit.company, company)
                .where(recruitLike(query))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(latestOrder())
                .fetch();

        Long total = queryFactory
                .select(recruit.count()).from(recruit)
                .join(recruit.company, company)
                .where(recruitLike(query))
                .fetchOne();
        return new PageImpl<>(recruitList, pageable, total == null ? 0 : total);
    }

    private BooleanExpression recruitLike(String query) {
        if (!StringUtils.hasText(query)) return null;
        return recruit.position.containsIgnoreCase(query)
                .or(recruit.skills.containsIgnoreCase(query))
                .or(company.name.containsIgnoreCase(query));
    }

    //생성일, 아이디 기준 내림차순 조회 (최신순)
    private OrderSpecifier<?>[] latestOrder() {
        return new OrderSpecifier[]{recruit.createdDate.desc(), recruit.id.desc()};
    }
}
