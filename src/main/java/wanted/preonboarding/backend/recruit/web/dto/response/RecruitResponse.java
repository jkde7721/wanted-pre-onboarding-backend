package wanted.preonboarding.backend.recruit.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.recruit.business.dto.response.RecruitWithAnotherResponse;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;

import java.util.List;

@Getter @Builder
public class RecruitResponse {

    private Long recruitId;

    private String companyName;

    private String nation;

    private String region;

    private String position;

    private Long compensationFee;

    private String skills;

    private String details;

    private List<Long> anotherRecruitList;

    public static RecruitResponse of(RecruitWithAnotherResponse recruitWithAnother) {
        Recruit recruit = recruitWithAnother.getRecruit();
        Company company = recruit.getCompany();
        List<Long> anotherRecruitList = recruitWithAnother.getAnotherRecruitList().stream().map(Recruit::getId).toList();
        return RecruitResponse.builder()
                .recruitId(recruit.getId())
                .companyName(company.getName())
                .nation(company.getNation())
                .region(company.getRegion())
                .position(recruit.getPosition())
                .compensationFee(recruit.getCompensationFee())
                .skills(recruit.getSkills())
                .details(recruit.getDetails())
                .anotherRecruitList(anotherRecruitList).build();
    }
}
