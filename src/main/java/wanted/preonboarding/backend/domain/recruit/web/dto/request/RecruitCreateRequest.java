package wanted.preonboarding.backend.domain.recruit.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.RecruitSaveRequest;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitCreateRequest {

    @NotNull(message = "채용공고의 회사 id를 입력해주세요.")
    private Long companyId;

    @NotBlank(message = "채용공고의 채용포지션을 입력해주세요.")
    private String position;

    @NotNull(message = "채용공고의 채용보상금을 입력해주세요.")
    private Long compensationFee;

    @NotBlank(message = "채용공고의 채용내용을 입력해주세요.")
    private String details;

    @NotBlank(message = "채용공고의 사용기술을 입력해주세요.")
    private String skills;

    public RecruitSaveRequest toServiceDto() {
        return new RecruitSaveRequest(position, compensationFee, details, skills);
    }
}
