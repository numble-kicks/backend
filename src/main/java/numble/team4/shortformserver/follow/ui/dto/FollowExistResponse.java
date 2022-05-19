package numble.team4.shortformserver.follow.ui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(NON_NULL)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowExistResponse {

    private final boolean existFollow;
    private final Long followId;

    public static FollowExistResponse from(Optional<Long> existFollowInfo) {
        return new FollowExistResponse(existFollowInfo.isPresent(), existFollowInfo.orElseGet(() -> null));
    }
}
