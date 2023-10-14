package wanted.preonboarding.backend.domain.apply.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCreateRequest {

    @NotNull(message = "지원서의 사용자 id를 입력해주세요.")
    private Long userId;

    @NotNull(message = "지원서의 채용공고 id를 입력해주세요.")
    private Long recruitId;
}
