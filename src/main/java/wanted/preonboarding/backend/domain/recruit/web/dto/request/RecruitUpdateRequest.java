package wanted.preonboarding.backend.domain.recruit.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.RecruitModifyRequest;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitUpdateRequest {

    @NotBlank(message = "채용공고의 채용포지션을 입력해주세요.")
    private String position;

    @NotNull(message = "채용공고의 채용보상금을 입력해주세요.")
    private Long compensationFee;

    @NotBlank(message = "채용공고의 채용내용을 입력해주세요.")
    private String details;

    @NotBlank(message = "채용공고의 사용기술을 입력해주세요.")
    private String skills;

    public RecruitModifyRequest toServiceDto() {
        return new RecruitModifyRequest(position, compensationFee, details, skills);
    }
}
