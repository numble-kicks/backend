package numble.team4.shortformserver.common.healthcheck;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HealthCheckResponseMessage {
    HEALTH_CHECK_MESSAGE("서버 정상 작동");

    private final String message;
}
