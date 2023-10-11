package wanted.preonboarding.backend.domain.user.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.global.exception.*;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;
import wanted.preonboarding.backend.domain.user.persistence.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wanted.preonboarding.backend.utils.Fixtures.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @DisplayName("해당 사용자 조회 성공 테스트")
    @Test
    void getUser() {
        //given
        User user = aUser().build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        //when
        User findedUser = userService.getUser(1L);

        //then
        verify(userRepository, times(1)).findById(anyLong());
        assertThat(findedUser).isEqualTo(user);
    }

    @DisplayName("해당 사용자 조회 실패 테스트")
    @Test
    void getUserFail() {
        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userService.getUser(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.USER_NOT_FOUND);
        verify(userRepository, times(1)).findById(anyLong());
    }
}