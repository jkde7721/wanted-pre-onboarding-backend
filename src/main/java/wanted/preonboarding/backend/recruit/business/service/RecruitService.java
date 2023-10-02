package wanted.preonboarding.backend.recruit.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.recruit.business.dto.request.RecruitSaveRequest;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.company.business.service.CompanyService;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.persistence.repository.RecruitRepository;

@RequiredArgsConstructor
@Service
public class RecruitService {

    private final CompanyService companyService;
    private final RecruitRepository recruitRepository;

    @Transactional
    public void registerRecruit(Long companyId, RecruitSaveRequest recruitSaveRequest) {
        Company company = companyService.getCompany(companyId);
        Recruit recruit = recruitSaveRequest.toEntity(company);
        recruitRepository.save(recruit);
    }
}
