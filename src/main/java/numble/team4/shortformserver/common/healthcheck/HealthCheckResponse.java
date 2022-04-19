package numble.team4.shortformserver.common.healthcheck;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HealthCheckResponse {
    private String name;
    private List<String> favoriteFoods;
    private List<String> favoriteSports;

    public static HealthCheckResponse of(String name, List<String> foods, List<String> sports) {
        return new HealthCheckResponse(name, foods, sports);
    }
}
