package numble.team4.shortformserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.healthcheck.HealthCheckException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    HEALTH_CHECK_EXCEPTION("예외 처리 정상 작동", INTERNAL_SERVER_ERROR, HealthCheckException.class);

    private final String message;
    private final HttpStatus status;
    private final Class<? extends Exception> type;

    public static ExceptionType findException(Class<? extends Exception> type) {
        return Arrays.stream(ExceptionType.values())
                .filter(exception -> exception.getType().equals(type))
                .findAny()
                .orElseThrow(UnexpectedException::new);
    }
}
