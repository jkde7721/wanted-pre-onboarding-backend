package wanted.preonboarding.backend.domain.company.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.domain.company.persistence.repository.CompanyRepository;
import wanted.preonboarding.backend.global.exception.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @DisplayName("ID로 회사 조회 실패 테스트")
    @Test
    void getCompanyFail() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCompany(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMPANY_NOT_FOUND);
    }
}