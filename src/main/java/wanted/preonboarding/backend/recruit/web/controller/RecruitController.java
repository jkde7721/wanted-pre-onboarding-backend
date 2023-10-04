package wanted.preonboarding.backend.recruit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.web.dto.request.*;
import wanted.preonboarding.backend.recruit.web.dto.response.*;

import java.util.List;

import static java.util.stream.Collectors.*;

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
    public List<RecruitListResponse> getRecruitList() {
        List<Recruit> recruitList = recruitService.getRecruitList();
        return recruitList.stream().map(RecruitListResponse::toDto).collect(toList());
    }
}
