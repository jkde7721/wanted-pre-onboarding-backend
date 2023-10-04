package wanted.preonboarding.backend.recruit.persistence.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;

import java.util.List;

import static wanted.preonboarding.backend.recruit.persistence.entity.QRecruit.*;

@RequiredArgsConstructor
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Recruit> findAllFetch() {
        return queryFactory.selectFrom(recruit)
                .join(recruit.company).fetchJoin()
                .orderBy(recruit.createdDate.desc(), recruit.id.desc()) //생성일, 아이디 기준 내림차순 조회 (최신순)
                .fetch();
    }
}
