package wanted.preonboarding.backend.user.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.user.persistence.entity.User;
import wanted.preonboarding.backend.user.persistence.repository.UserRepository;

import static wanted.preonboarding.backend.exception.ErrorCode.*;

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
