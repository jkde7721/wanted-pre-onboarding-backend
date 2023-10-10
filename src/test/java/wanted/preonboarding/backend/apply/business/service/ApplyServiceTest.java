package wanted.preonboarding.backend.apply.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.apply.persistence.entity.Apply;
import wanted.preonboarding.backend.apply.persistence.repository.ApplyRepository;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.user.business.service.UserService;
import wanted.preonboarding.backend.user.persistence.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wanted.preonboarding.backend.exception.ErrorCode.*;

@ExtendWith(MockitoExtension.class)
class ApplyServiceTest {

    @InjectMocks
    ApplyService applyService;

    @Mock
    UserService userService;

    @Mock
    RecruitService recruitService;

    @Mock
    ApplyRepository applyRepository;

    @Captor
    ArgumentCaptor<Apply> applyCaptor;

    @DisplayName("해당 채용공고에 지원 성공 테스트")
    @Test
    void applyRecruit() {
        //given
        User user = User.builder().id(1L).name("Ethan").careerYear(1).build();
        Recruit recruit = Recruit.builder().id(1L).company(null)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Apply apply = Apply.builder().id(1L).user(user).recruit(recruit).build();
        when(userService.getUser(anyLong())).thenReturn(user);
        when(recruitService.getRecruit(anyLong())).thenReturn(recruit);
        when(applyRepository.findByUserAndRecruit(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(applyRepository.save(any(Apply.class))).thenReturn(apply);

        //when
        Long applyId = applyService.applyRecruit(1L, 1L);

        //then
        verify(userService, times(1)).getUser(anyLong());
        verify(recruitService, times(1)).getRecruit(anyLong());
        verify(applyRepository, times(1)).findByUserAndRecruit(anyLong(), anyLong());
        verify(applyRepository, times(1)).save(applyCaptor.capture());
        assertThat(applyId).isEqualTo(1L);
        assertThat(applyCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(applyCaptor.getValue().getRecruit()).isEqualTo(recruit);
    }

    @DisplayName("해당 채용공고에 지원 실패 테스트 - 해당 사용자 존재하지 않음")
    @Test
    void applyRecruitFailNoUser() {
        //given
        when(userService.getUser(anyLong())).thenThrow(new BusinessException(USER_NOT_FOUND));

        //when, then
        assertThatThrownBy(() -> applyService.applyRecruit(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(USER_NOT_FOUND);
        verify(userService, times(1)).getUser(anyLong());
        verify(recruitService, times(0)).getRecruit(anyLong());
        verify(applyRepository, times(0)).findByUserAndRecruit(anyLong(), anyLong());
        verify(applyRepository, times(0)).save(any(Apply.class));
    }

    @DisplayName("해당 채용공고에 지원 실패 테스트 - 해당 채용공고 존재하지 않음")
    @Test
    void applyRecruitFailNoRecruit() {
        //given
        when(recruitService.getRecruit(anyLong())).thenThrow(new BusinessException(RECRUIT_NOT_FOUND));

        //when, then
        assertThatThrownBy(() -> applyService.applyRecruit(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(RECRUIT_NOT_FOUND);
        verify(userService, times(1)).getUser(anyLong());
        verify(recruitService, times(1)).getRecruit(anyLong());
        verify(applyRepository, times(0)).findByUserAndRecruit(anyLong(), anyLong());
        verify(applyRepository, times(0)).save(any(Apply.class));
    }

    @DisplayName("해당 채용공고에 지원 실패 테스트 - 해당 채용공고 지원내역 이미 존재")
    @Test
    void applyRecruitFailDoubleApply() {
        //given
        User user = User.builder().id(1L).name("Ethan").careerYear(1).build();
        Recruit recruit = Recruit.builder().id(1L).company(null)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Apply apply = Apply.builder().id(1L).user(user).recruit(recruit).build();
        when(applyRepository.findByUserAndRecruit(anyLong(), anyLong())).thenReturn(Optional.of(apply));

        //when, then
        assertThatThrownBy(() -> applyService.applyRecruit(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ALREADY_EXIST_APPLY);
        verify(userService, times(1)).getUser(anyLong());
        verify(recruitService, times(1)).getRecruit(anyLong());
        verify(applyRepository, times(1)).findByUserAndRecruit(anyLong(), anyLong());
        verify(applyRepository, times(0)).save(any(Apply.class));
    }

    @DisplayName("해당 사용자가 해당 채용공고에 지원한 내역이 존재하는지 체크")
    @Test
    void isExistingApply() {
        //given
        User user = User.builder().id(1L).name("Ethan").careerYear(1).build();
        Recruit recruit = Recruit.builder().id(1L).company(null)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Apply apply = Apply.builder().id(1L).user(user).recruit(recruit).build();
        when(applyRepository.findByUserAndRecruit(anyLong(), anyLong())).thenReturn(Optional.of(apply));

        //when
        boolean existingApply = applyService.isExistingApply(1L, 1L);

        // then
        verify(applyRepository, times(1)).findByUserAndRecruit(anyLong(), anyLong());
        assertThat(existingApply).isTrue();
    }

    @DisplayName("해당 사용자가 해당 채용공고에 지원한 내역이 존재하지 않는지 체크")
    @Test
    void isNotExistingApply() {
        //given
        when(applyRepository.findByUserAndRecruit(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when
        boolean existingApply = applyService.isExistingApply(1L, 1L);

        // then
        verify(applyRepository, times(1)).findByUserAndRecruit(anyLong(), anyLong());
        assertThat(existingApply).isFalse();
    }
}