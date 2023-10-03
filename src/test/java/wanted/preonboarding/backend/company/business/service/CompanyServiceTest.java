package wanted.preonboarding.backend.company.business.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import wanted.preonboarding.backend.company.persistence.repository.CompanyRepository;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.exception.ErrorCode;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;

    @Mock
    CompanyRepository companyRepository;

    @Test
    void getCompanyFail() {
        Mockito.when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> companyService.getCompany(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COMPANY_NOT_FOUND);
    }
}