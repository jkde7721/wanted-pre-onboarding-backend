package wanted.preonboarding.backend.domain.recruit.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.response.*;
import wanted.preonboarding.backend.domain.recruit.persistence.repository.RecruitRepository;
import wanted.preonboarding.backend.domain.recruit.web.dto.response.*;
import wanted.preonboarding.backend.global.exception.BusinessException;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.company.business.service.CompanyService;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

import java.util.List;

import static wanted.preonboarding.backend.global.exception.ErrorCode.*;

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

    @Transactional
    public void removeRecruit(Long recruitId) {
        Recruit recruit = getRecruit(recruitId);
        recruitRepository.delete(recruit);
    }

    @Transactional(readOnly = true)
    public Recruit getRecruit(Long recruitId) {
        return recruitRepository.findById(recruitId)
                .orElseThrow(() -> new BusinessException(RECRUIT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<RecruitListResponse> getRecruitList(Pageable pageable) {
        return recruitRepository.findAllFetch(pageable);
    }

    @Transactional(readOnly = true)
    public RecruitWithAnotherResponse getRecruitWithAnotherOfTheCompany(Long recruitId) {
        Recruit recruit = recruitRepository.findByIdFetch(recruitId)
                .orElseThrow(() -> new BusinessException(RECRUIT_NOT_FOUND));
        List<Recruit> anotherRecruitList = recruitRepository.findByCompanyNotEqualRecruitOrderByLatest(recruit.getCompany().getId(), recruitId);
        return new RecruitWithAnotherResponse(recruit, anotherRecruitList);
    }

    @Transactional(readOnly = true)
    public Page<RecruitListSearchResponse> searchRecruitListBy(String query, Pageable pageable) {
        return recruitRepository.findByQueryFetch(query, pageable);
    }
}
