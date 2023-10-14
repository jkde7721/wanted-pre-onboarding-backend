package wanted.preonboarding.backend.domain.recruit.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.RecruitModifyRequest;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitUpdateRequest {

    @NotBlank(message = "{recruit.position.notBlank}")
    private String position;

    @NotNull(message = "{recruit.compensationFee.notNull}")
    private Long compensationFee;

    @NotBlank(message = "{recruit.details.notBlank}")
    private String details;

    @NotBlank(message = "{recruit.skills.notBlank}")
    private String skills;

    public RecruitModifyRequest toServiceDto() {
        return new RecruitModifyRequest(position, compensationFee, details, skills);
    }
}
