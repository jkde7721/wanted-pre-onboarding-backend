package wanted.preonboarding.backend.domain.recruit.business.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

import java.util.List;

@Getter
@AllArgsConstructor
public class RecruitWithAnotherResponse {

    private Recruit recruit;
    private List<Recruit> anotherRecruitList;
}
