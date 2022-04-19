package numble.team4.shortformserver.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionController {
    public static final String VALID_ERROR_MESSAGE = "요청 양식을 확인해주세요.";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponse> baseExceptionHandle(BaseException exception) {
        return new ResponseEntity<>(BaseErrorResponse.from(exception), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(BaseErrorResponse.of(VALID_ERROR_MESSAGE, errors), BAD_REQUEST);
    }
}
