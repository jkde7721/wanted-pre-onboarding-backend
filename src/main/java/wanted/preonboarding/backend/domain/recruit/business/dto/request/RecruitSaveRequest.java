package wanted.preonboarding.backend.domain.recruit.business.dto.request;

import lombok.AllArgsConstructor;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

@AllArgsConstructor
public class RecruitSaveRequest {

    private String position;
    private Long compensationFee;
    private String details;
    private String skills;

    public Recruit toEntity(Company company) {
        return Recruit.builder().company(company)
                .position(position)
                .compensationFee(compensationFee)
                .details(details)
                .skills(skills).build();
    }
}
