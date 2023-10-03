package wanted.preonboarding.backend.recruit.web.dto.request;

import lombok.*;
import wanted.preonboarding.backend.recruit.business.dto.request.RecruitSaveRequest;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitCreateRequest {

    private Long companyId;

    private String position;

    private Long compensationFee;

    private String details;

    private String skills;

    public RecruitSaveRequest toServiceDto() {
        return new RecruitSaveRequest(position, compensationFee, details, skills);
    }
}
