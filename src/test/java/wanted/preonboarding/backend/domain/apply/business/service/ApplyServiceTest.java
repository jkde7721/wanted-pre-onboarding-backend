package wanted.preonboarding.backend.domain.apply.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.domain.apply.persistence.entity.Apply;
import wanted.preonboarding.backend.domain.apply.persistence.repository.ApplyRepository;
import wanted.preonboarding.backend.global.exception.BusinessException;
import wanted.preonboarding.backend.domain.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.user.business.service.UserService;
import wanted.preonboarding.backend.domain.user.persistence.entity.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wanted.preonboarding.backend.global.exception.ErrorCode.*;
import static wanted.preonboarding.backend.utils.Fixtures.*;

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
        User user = aUser().build();
        Recruit recruit = aRecruit().build();
        Apply apply = aApply().user(user).recruit(recruit).build();
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
        assertThat(applyId).isEqualTo(apply.getId());
        assertThat(applyCaptor.getValue().getUser()).isEqualTo(user); //생성된 지원내역과 해당 사용자와의 연관관계 검증
        assertThat(applyCaptor.getValue().getRecruit()).isEqualTo(recruit); //생성된 지원내역과 해당 채용공고와의 연관관계 검증
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
        Apply apply = aApply().build();
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
        Apply apply = aApply().build();
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