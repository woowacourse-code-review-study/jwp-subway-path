package subway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.exception.ApiException;
import subway.exception.ApiIllegalArgumentException;
import subway.exception.ApiNoSuchResourceException;
import subway.exception.ErrorResponse;


@ControllerAdvice
public class GlobalExceptionAdvice {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError().body(ErrorResponse.from("서버에서 오류가 발생했습니다."));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final ApiException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final ApiIllegalArgumentException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final ApiNoSuchResourceException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.from(e.getMessage()));
    }
}
