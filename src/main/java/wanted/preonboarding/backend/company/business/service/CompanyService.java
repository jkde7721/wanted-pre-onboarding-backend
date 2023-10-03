package wanted.preonboarding.backend.company.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.company.persistence.repository.CompanyRepository;
import wanted.preonboarding.backend.exception.BusinessException;

import static wanted.preonboarding.backend.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company getCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new BusinessException(COMPANY_NOT_FOUND));
    }
}
