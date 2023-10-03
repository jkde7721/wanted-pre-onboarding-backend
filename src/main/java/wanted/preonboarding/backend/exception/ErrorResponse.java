package wanted.preonboarding.backend.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        timestamp = LocalDateTime.now();
        status = errorCode.getHttpStatus().value();
        error = errorCode.getHttpStatus().name();
        code = errorCode.name();
        message = errorCode.getMessage();
    }
}
