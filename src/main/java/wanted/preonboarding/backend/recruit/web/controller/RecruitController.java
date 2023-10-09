package wanted.preonboarding.backend.recruit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.web.dto.request.*;
import wanted.preonboarding.backend.recruit.web.dto.response.*;

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
    public Page<RecruitListResponse> getRecruitList(Pageable pageable) {
        return recruitService.getRecruitList(pageable);
    }
}
