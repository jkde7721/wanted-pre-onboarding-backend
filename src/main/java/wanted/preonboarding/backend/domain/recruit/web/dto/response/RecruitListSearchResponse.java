package wanted.preonboarding.backend.domain.recruit.web.dto.response;

import lombok.*;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

@Getter @Builder
@AllArgsConstructor
public class RecruitListSearchResponse {

    private Long recruitId;

    private String companyName;

    private String nation;

    private String region;

    private String position;

    private Long compensationFee;

    private String skills;

    public static RecruitListSearchResponse toDto(Recruit recruit) {
        Company company = recruit.getCompany();
        return RecruitListSearchResponse.builder()
                .recruitId(recruit.getId())
                .companyName(company.getName())
                .nation(company.getNation())
                .region(company.getRegion())
                .position(recruit.getPosition())
                .compensationFee(recruit.getCompensationFee())
                .skills(recruit.getSkills()).build();
    }
}
