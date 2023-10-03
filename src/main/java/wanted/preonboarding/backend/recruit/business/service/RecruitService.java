package wanted.preonboarding.backend.recruit.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.recruit.business.dto.request.*;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.company.business.service.CompanyService;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.persistence.repository.RecruitRepository;

import static wanted.preonboarding.backend.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class RecruitService {

    private final CompanyService companyService;
    private final RecruitRepository recruitRepository;

    @Transactional
    public Long registerRecruit(Long companyId, RecruitSaveRequest recruitSaveRequest) {
        Company company = companyService.getCompany(companyId);
        Recruit recruit = recruitSaveRequest.toEntity(company);
        return recruitRepository.save(recruit).getId();
    }

    @Transactional
    public void modifyRecruit(Long recruitId, RecruitModifyRequest recruitModifyRequest) {
        Recruit recruit = getRecruit(recruitId);
        recruit.modify(recruitModifyRequest.getPosition(), recruitModifyRequest.getCompensationFee(),
                recruitModifyRequest.getDetails(), recruitModifyRequest.getSkills());
    }

    public Recruit getRecruit(Long recruitId) {
        return recruitRepository.findById(recruitId)
                .orElseThrow(() -> new BusinessException(RECRUIT_NOT_FOUND));
    }
}
