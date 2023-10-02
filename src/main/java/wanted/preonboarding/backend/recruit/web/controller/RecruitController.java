package wanted.preonboarding.backend.recruit.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.web.dto.request.RecruitCreateRequest;

@RequiredArgsConstructor
@RequestMapping("/recruits")
@RestController
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping
    public ResponseEntity<Void> registerRecruit(@RequestBody RecruitCreateRequest recruitCreateRequest) {
        recruitService.registerRecruit(recruitCreateRequest.getCompanyId(), recruitCreateRequest.toServiceDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
