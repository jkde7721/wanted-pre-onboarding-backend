package wanted.preonboarding.backend.domain.recruit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.response.RecruitWithAnotherResponse;
import wanted.preonboarding.backend.domain.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.domain.recruit.web.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.web.dto.response.*;
import wanted.preonboarding.backend.global.paging.*;

@RequiredArgsConstructor
@RequestMapping("/recruits")
@RestController
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping
    public ResponseEntity<RecruitCreateResponse> registerRecruit(@RequestBody RecruitCreateRequest recruitCreateRequest) {
        Long recruitId = recruitService.registerRecruit(recruitCreateRequest.getCompanyId(), recruitCreateRequest.toServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RecruitCreateResponse(recruitId));
    }

    @PutMapping("/{recruitId}")
    public void modifyRecruit(@PathVariable Long recruitId,
                              @RequestBody RecruitUpdateRequest recruitUpdateRequest) {
        recruitService.modifyRecruit(recruitId, recruitUpdateRequest.toServiceDto());
    }

    @DeleteMapping("/{recruitId}")
    public ResponseEntity<Void> removeRecruit(@PathVariable Long recruitId) {
        recruitService.removeRecruit(recruitId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public PageResponse<RecruitListResponse> getRecruitList(PageRequest pageRequest) {
        return recruitService.getRecruitList(pageRequest);
    }

    @GetMapping("/{recruitId}")
    public RecruitResponse getRecruitWithAnotherOfTheCompany(@PathVariable Long recruitId) {
        RecruitWithAnotherResponse recruitWithAnother = recruitService.getRecruitWithAnotherOfTheCompany(recruitId);
        return RecruitResponse.of(recruitWithAnother);
    }

    @GetMapping("/search")
    public PageResponse<RecruitListSearchResponse> searchRecruitListBy(@RequestParam(required = false) String query, PageRequest pageRequest) {
        return recruitService.searchRecruitListBy(query, pageRequest);
    }
}
