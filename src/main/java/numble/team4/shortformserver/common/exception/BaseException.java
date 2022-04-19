package numble.team4.shortformserver.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class BaseException extends RuntimeException {

    private final ExceptionType exceptionType = ExceptionType.findException(this.getClass());

    private final String message;
    private final LocalDateTime timestamp;
    private final HttpStatus status;

    public BaseException() {
        this.message = exceptionType.getMessage();
        this.timestamp = LocalDateTime.now();
        this.status = exceptionType.getStatus();
    }
}
