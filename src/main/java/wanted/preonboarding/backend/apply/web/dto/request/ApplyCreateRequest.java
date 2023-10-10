package wanted.preonboarding.backend.apply.web.dto.request;

import lombok.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCreateRequest {

    private Long userId;

    private Long recruitId;
}
