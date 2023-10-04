package wanted.preonboarding.backend.recruit.web.dto.response;

import lombok.Builder;
import lombok.Getter;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;

@Getter @Builder
public class RecruitListResponse {

    private Long recruitId;

    private String companyName;

    private String nation;

    private String region;

    private String position;

    private Long compensationFee;

    private String skills;

    public static RecruitListResponse toDto(Recruit recruit) {
        Company company = recruit.getCompany();
        return RecruitListResponse.builder()
                .recruitId(recruit.getId())
                .companyName(company.getName())
                .nation(company.getNation())
                .region(company.getRegion())
                .position(recruit.getPosition())
                .compensationFee(recruit.getCompensationFee())
                .skills(recruit.getSkills()).build();
    }
}
