package wanted.preonboarding.backend.domain.company.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.company.persistence.repository.CompanyRepository;
import wanted.preonboarding.backend.global.exception.BusinessException;

import static wanted.preonboarding.backend.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException(COMPANY_NOT_FOUND));
    }
}
