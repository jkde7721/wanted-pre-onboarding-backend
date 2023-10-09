package wanted.preonboarding.backend.recruit.business.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.company.business.service.CompanyService;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.exception.*;
import wanted.preonboarding.backend.recruit.business.dto.request.*;
import wanted.preonboarding.backend.recruit.business.dto.response.RecruitWithAnotherResponse;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.persistence.repository.RecruitRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.*;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @InjectMocks
    RecruitService recruitService;

    @Mock
    CompanyService companyService;

    @Mock
    RecruitRepository recruitRepository;

    @DisplayName("채용공고 등록 성공 테스트")
    @Test
    void registerRecruit() {
        //given
        Company company = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Recruit recruit = Recruit.builder().id(1L).company(company)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..").skills("Python").build();
        Mockito.when(companyService.getCompany(anyLong())).thenReturn(company);
        Mockito.when(recruitRepository.save(any(Recruit.class))).thenReturn(recruit);

        //when
        RecruitSaveRequest recruitSaveRequest = new RecruitSaveRequest("백엔드 주니어 개발자", 1000000L, "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..", "Python");
        Long recruitId = recruitService.registerRecruit(1L, recruitSaveRequest);

        //then
        assertThat(recruitId).isEqualTo(recruit.getId());
        verify(companyService, times(1)).getCompany(anyLong());
        verify(recruitRepository, times(1)).save(any(Recruit.class));
    }

    @DisplayName("채용공고 수정 성공 테스트")
    @Test
    void modifyRecruit() {
        //given
        Company company = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Recruit recruit = Recruit.builder().id(1L).company(company)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..").skills("Python").build();
        Mockito.when(recruitRepository.findById(anyLong())).thenReturn(Optional.of(recruit));

        //when
        RecruitModifyRequest recruitModifyRequest = new RecruitModifyRequest("백엔드 시니어 개발자", 1500000L, "네이버에서 백엔드 시니어 개발자를 채용합니다.", "Spring");
        recruitService.modifyRecruit(1L, recruitModifyRequest);

        //then
        assertThat(recruit)
                .extracting("position", "compensationFee", "details", "skills")
                .containsExactly(recruitModifyRequest.getPosition(), recruitModifyRequest.getCompensationFee(), recruitModifyRequest.getDetails(), recruitModifyRequest.getSkills());
        verify(recruitRepository, times(1)).findById(anyLong());
    }

    @DisplayName("채용공고 삭제 성공 테스트")
    @Test
    void removeRecruit() {
        //given
        Company company = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Recruit recruit = Recruit.builder().id(1L).company(company)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..").skills("Python").build();
        Mockito.when(recruitRepository.findById(anyLong())).thenReturn(Optional.of(recruit));

        //when
        recruitService.removeRecruit(1L);

        //then
        verify(recruitRepository, times(1)).delete(recruit);
    }

    @DisplayName("ID로 채용공고 조회 실패 테스트")
    @Test
    void getRecruitFail() {
        Mockito.when(recruitRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recruitService.getRecruit(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RECRUIT_NOT_FOUND);
    }

    @DisplayName("특정 채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 1개 이상")
    @Test
    void getRecruitWithAnotherOfTheCompany() {
        //given
        Company company = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Recruit recruit1 = Recruit.builder().id(1L).company(company)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Recruit recruit2 = Recruit.builder().id(2L).company(company)
                .position("백엔드 시니어 개발자").compensationFee(1500000L)
                .details("원티드랩에서 백엔드 시니어 개발자를 채용합니다.").skills("Spring").build();
        Recruit recruit3 = Recruit.builder().id(3L).company(company)
                .position("프론트엔드 시니어 개발자").compensationFee(1500000L)
                .details("네이버에서 프론트엔드 시니어 개발자를 채용합니다.").skills("React").build();
        Mockito.when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(recruit1));
        Mockito.when(recruitRepository.findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong())).thenReturn(List.of(recruit2, recruit3));

        //when
        RecruitWithAnotherResponse recruitWithAnother = recruitService.getRecruitWithAnotherOfTheCompany(1L);

        //then
        verify(recruitRepository, times(1)).findByIdFetch(anyLong());
        verify(recruitRepository, times(1)).findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong());
        assertThat(recruitWithAnother.getRecruit()).isEqualTo(recruit1);
        assertThat(recruitWithAnother.getAnotherRecruitList()).hasSize(2);
        assertThat(recruitWithAnother.getAnotherRecruitList()).containsExactly(recruit2, recruit3);
    }

    @DisplayName("특정 채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 0개")
    @Test
    void getRecruitWithAnotherOfTheCompanyWithZeroAnother() {
        //given
        Company company = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Recruit recruit = Recruit.builder().id(1L).company(company)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Mockito.when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(recruit));
        Mockito.when(recruitRepository.findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong())).thenReturn(List.of());

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
        Mockito.when(recruitRepository.findByIdFetch(anyLong())).thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> recruitService.getRecruitWithAnotherOfTheCompany(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RECRUIT_NOT_FOUND);
        verify(recruitRepository, times(1)).findByIdFetch(anyLong());
        verify(recruitRepository, times(0)).findByCompanyNotEqualRecruitOrderByLatest(anyLong(), anyLong());
    }
}
