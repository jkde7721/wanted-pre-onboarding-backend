package wanted.preonboarding.backend.domain.apply.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.preonboarding.backend.domain.apply.business.service.ApplyService;
import wanted.preonboarding.backend.domain.apply.web.dto.request.*;
import wanted.preonboarding.backend.domain.apply.web.dto.response.*;

@RequiredArgsConstructor
@RequestMapping("/applies")
@RestController
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping
    public ResponseEntity<ApplyCreateResponse> applyRecruit(@Valid @RequestBody ApplyCreateRequest applyCreateRequest) {
        Long applyId = applyService.applyRecruit(applyCreateRequest.getUserId(), applyCreateRequest.getRecruitId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApplyCreateResponse(applyId));
    }
}
