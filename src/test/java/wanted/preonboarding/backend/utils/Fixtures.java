package wanted.preonboarding.backend.utils;

import wanted.preonboarding.backend.domain.apply.persistence.entity.Apply;
import wanted.preonboarding.backend.domain.apply.persistence.entity.Apply.ApplyBuilder;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company.CompanyBuilder;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit.RecruitBuilder;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;
import wanted.preonboarding.backend.domain.user.persistence.entity.User.UserBuilder;

public class Fixtures {

    public static CompanyBuilder aCompany() {
        return Company.builder()
                .id(1L)
                .name("원티드랩")
                .nation("한국")
                .region("서울");
    }

    public static CompanyBuilder aCompany2() {
        return Company.builder()
                .id(2L)
                .name("네이버")
                .nation("한국")
                .region("판교");
    }

    public static RecruitBuilder aRecruit() {
        return Recruit.builder()
                .id(1L)
                .company(aCompany().build())
                .position("백엔드 주니어 개발자")
                .compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.")
                .skills("Python");
    }

    public static RecruitBuilder aRecruit2() {
        return Recruit.builder()
                .id(2L)
                .company(aCompany().build())
                .position("백엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("원티드랩에서 백엔드 시니어 개발자를 채용합니다.")
                .skills("Spring");
    }

    public static RecruitBuilder aRecruit3() {
        return Recruit.builder()
                .id(3L)
                .company(aCompany().build())
                .position("프론트엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("원티드랩에서 프론트엔드 시니어 개발자를 채용합니다.")
                .skills("React");
    }

    public static RecruitBuilder aRecruit4() {
        return Recruit.builder()
                .id(4L)
                .company(aCompany2().build())
                .position("프론트엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("네이버에서 React 개발자를 채용합니다.")
                .skills("React");
    }

    public static UserBuilder aUser() {
        return User.builder()
                .id(1L)
                .name("Ethan")
                .careerYear(1);
    }

    public static ApplyBuilder aApply() {
        return Apply.builder()
                .id(1L)
                .user(aUser().build())
                .recruit(aRecruit().build());
    }
}
