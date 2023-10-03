package wanted.preonboarding.backend.recruit.web.dto.request;

import lombok.*;
import wanted.preonboarding.backend.recruit.business.dto.request.RecruitModifyRequest;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitUpdateRequest {

    private String position;

    private Long compensationFee;

    private String details;

    private String skills;

    public RecruitModifyRequest toServiceDto() {
        return new RecruitModifyRequest(position, compensationFee, details, skills);
    }
}
