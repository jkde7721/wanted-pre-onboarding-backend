package wanted.preonboarding.backend.recruit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.web.dto.request.RecruitCreateRequest;
import wanted.preonboarding.backend.recruit.web.dto.response.RecruitCreateResponse;

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
}