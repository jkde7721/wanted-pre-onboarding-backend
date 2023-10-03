package wanted.preonboarding.backend.recruit.business.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.company.business.service.CompanyService;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.recruit.business.dto.request.RecruitSaveRequest;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.persistence.repository.RecruitRepository;

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
}