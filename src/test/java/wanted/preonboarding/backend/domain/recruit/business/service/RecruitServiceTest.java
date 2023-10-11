package wanted.preonboarding.backend.domain.recruit.business.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.domain.company.business.service.CompanyService;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.response.*;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.domain.recruit.persistence.repository.RecruitRepository;
import wanted.preonboarding.backend.global.exception.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static wanted.preonboarding.backend.utils.Fixtures.*;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @InjectMocks
    RecruitService recruitService;

    @Mock
    CompanyService companyService;

    @Mock
    RecruitRepository recruitRepository;

    @Captor
    ArgumentCaptor<Recruit> recruitCaptor;

    @DisplayName("채용공고 등록 성공 테스트")
    @Test
    void registerRecruit() {
        //given
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        when(companyService.getCompany(anyLong())).thenReturn(company);
        when(recruitRepository.save(any(Recruit.class))).thenReturn(recruit);

        //when
        RecruitSaveRequest recruitSaveRequest = new RecruitSaveRequest(recruit.getPosition(), recruit.getCompensationFee(), recruit.getDetails(), recruit.getSkills());
        Long recruitId = recruitService.registerRecruit(1L, recruitSaveRequest);

        //then
        verify(companyService, times(1)).getCompany(anyLong());
        verify(recruitRepository, times(1)).save(recruitCaptor.capture());
        assertThat(recruitId).isEqualTo(recruit.getId());
        assertThat(recruitCaptor.getValue().getCompany()).isEqualTo(company); //생성된 채용공고와 해당 회사와의 연관관계 검증
    }

    @DisplayName("채용공고 수정 성공 테스트")
    @Test
    void modifyRecruit() {
        //given
        Recruit recruit = aRecruit()
                .position("백엔드 주니어 개발자")
                .compensationFee(1000000L)
                .skills("Python").build();
        when(recruitRepository.findById(anyLong())).thenReturn(Optional.of(recruit));

        //when
        RecruitModifyRequest recruitModifyRequest = new RecruitModifyRequest("백엔드 시니어 개발자", 1500000L, recruit.getDetails(), "Spring");
        recruitService.modifyRecruit(1L, recruitModifyRequest);

        //then
        verify(recruitRepository, times(1)).findById(anyLong());
        assertThat(recruit)
                .extracting("position", "compensationFee", "details", "skills")
                .containsExactly(recruitModifyRequest.getPosition(), recruitModifyRequest.getCompensationFee(), recruitModifyRequest.getDetails(), recruitModifyRequest.getSkills());
    }

    @DisplayName("채용공고 삭제 성공 테스트")
    @Test
    void removeRecruit() {
        //given
        Recruit recruit = aRecruit().build();
        when(recruitRepository.findById(anyLong())).thenReturn(Optional.of(recruit));

        //when
        recruitService.removeRecruit(1L);

        //then
        verify(recruitRepository, times(1)).delete(recruit);
    }

    @DisplayName("ID로 채용공고 조회 실패 테스트")
    @Test
    void getRecruitFail() {
        //given
        when(recruitRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> recruitService.getRecruit(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RECRUIT_NOT_FOUND);
    }

    @DisplayName("특정 채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 1개 이상")
    @Test
    void getRecruitWithAnotherOfTheCompany() {
        //given
        Recruit recruit = aRecruit().build();
        Recruit recruit2 = aRecruit2().build();
        Recruit recruit3 = aRecruit3().build();
        when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(recruit));
        when(recruitRepository.findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong())).thenReturn(List.of(recruit2, recruit3));

        //when
        RecruitWithAnotherResponse recruitWithAnother = recruitService.getRecruitWithAnotherOfTheCompany(1L);

        //then
        verify(recruitRepository, times(1)).findByIdFetch(anyLong());
        verify(recruitRepository, times(1)).findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong());
        assertThat(recruitWithAnother.getRecruit()).isEqualTo(recruit);
        assertThat(recruitWithAnother.getAnotherRecruitList()).hasSize(2);
        assertThat(recruitWithAnother.getAnotherRecruitList()).containsExactly(recruit2, recruit3);
    }

    @DisplayName("특정 채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 0개")
    @Test
    void getRecruitWithAnotherOfTheCompanyWithZeroAnother() {
        //given
        Recruit recruit = aRecruit().build();
        when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(recruit));
        when(recruitRepository.findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong())).thenReturn(List.of());

        //when
        RecruitWithAnotherResponse recruitWithAnother = recruitService.getRecruitWithAnotherOfTheCompany(1L);

        //then
        verify(recruitRepository, times(1)).findByIdFetch(anyLong());
        verify(recruitRepository, times(1)).findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong());
        assertThat(recruitWithAnother.getRecruit()).isEqualTo(recruit);
        assertThat(recruitWithAnother.getAnotherRecruitList()).isEmpty();
    }

    @DisplayName("특정 채용공고 상세 조회 실패 테스트")
    @Test
    void getRecruitWithAnotherOfTheCompanyFail() {
        //given
        when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> recruitService.getRecruitWithAnotherOfTheCompany(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RECRUIT_NOT_FOUND);
        verify(recruitRepository, times(1)).findByIdFetch(anyLong());
        verify(recruitRepository, times(0)).findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong());
    }
}
