package numble.team4.shortformserver.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class BaseErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private Map<String, String> errors;

    public static BaseErrorResponse of(String message, Map<String, String> errors) {
        return new BaseErrorResponse(
                message,
                LocalDateTime.now(),
                BAD_REQUEST.value(),
                BAD_REQUEST.getReasonPhrase(),
                errors);
    }

    public static BaseErrorResponse from(BaseException exception) {
        return new BaseErrorResponse(
                exception.getMessage(),
                exception.getTimestamp(),
                exception.getStatus().value(),
                exception.getStatus().getReasonPhrase(),
                null);
    }
}
