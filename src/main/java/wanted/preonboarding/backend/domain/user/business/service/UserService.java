package wanted.preonboarding.backend.domain.user.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;
import wanted.preonboarding.backend.domain.user.persistence.repository.UserRepository;
import wanted.preonboarding.backend.global.exception.BusinessException;

import static wanted.preonboarding.backend.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
    }
}
