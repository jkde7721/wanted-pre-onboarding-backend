package wanted.preonboarding.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    RECRUIT_NOT_FOUND(NOT_FOUND, "해당 채용공고를 찾을 수 없습니다."),
    COMPANY_NOT_FOUND(NOT_FOUND, "해당 회사를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
