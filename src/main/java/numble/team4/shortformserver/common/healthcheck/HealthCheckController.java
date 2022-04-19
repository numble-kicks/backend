package numble.team4.shortformserver.common.healthcheck;

import numble.team4.shortformserver.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static numble.team4.shortformserver.common.healthcheck.HealthCheckResponseMessage.HEALTH_CHECK_MESSAGE;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckController {

    @GetMapping("/normal")
    public CommonResponse<HealthCheckResponse> healthCheckNormalCase() {
        String username = "테스트 유저";
        List<String> foods = Arrays.asList("치킨", "피자", "족발");
        List<String> sports = Arrays.asList("축구", "야구", "농구");

        return CommonResponse.of(HealthCheckResponse.of(username, foods, sports), HEALTH_CHECK_MESSAGE.getMessage());
    }

    @GetMapping("/exception")
    public CommonResponse<HealthCheckResponse> healthCheckException() {
        throw new HealthCheckException();
    }

    @GetMapping("/require-auth")
    public CommonResponse<Void> permitUrlTest() {
        return CommonResponse.from("security permitAll test");
    }
}
