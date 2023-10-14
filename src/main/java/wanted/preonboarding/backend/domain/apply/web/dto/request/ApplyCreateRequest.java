package wanted.preonboarding.backend.domain.apply.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCreateRequest {

    @NotNull(message = "{apply.userId.notNull}")
    private Long userId;

    @NotNull(message = "{apply.recruitId.notNull}")
    private Long recruitId;
}
