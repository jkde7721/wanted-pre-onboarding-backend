package wanted.preonboarding.backend.domain.recruit.business.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecruitModifyRequest {

    private String position;

    private Long compensationFee;

    private String details;

    private String skills;
}
