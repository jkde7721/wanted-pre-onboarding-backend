package wanted.preonboarding.backend.domain.apply.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.domain.apply.persistence.entity.Apply;
import wanted.preonboarding.backend.domain.apply.persistence.repository.ApplyRepository;
import wanted.preonboarding.backend.domain.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;
import wanted.preonboarding.backend.global.exception.BusinessException;
import wanted.preonboarding.backend.domain.user.business.service.UserService;

import static wanted.preonboarding.backend.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ApplyService {

    private final UserService userService;
    private final RecruitService recruitService;
    private final ApplyRepository applyRepository;

    @Transactional
    public Long applyRecruit(Long userId, Long recruitId) {
        User user = userService.getUser(userId);
        Recruit recruit = recruitService.getRecruit(recruitId);
        if (isExistingApply(userId, recruitId)) throw new BusinessException(ALREADY_EXIST_APPLY);

        Apply apply = Apply.builder().user(user).recruit(recruit).build();
        return applyRepository.save(apply).getId();
    }

    @Transactional(readOnly = true)
    public boolean isExistingApply(Long userId, Long recruitId) {
        return applyRepository.findByUserAndRecruit(userId, recruitId).isPresent();
    }
}
