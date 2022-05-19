package numble.team4.shortformserver.likevideo.ui.dto;

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
public class LikeVideoExistResponse {

    private final boolean existLikeVideo;
    private final Long likesId;

    public static LikeVideoExistResponse from(Optional<Long> existVideoInfo) {
        return new LikeVideoExistResponse(existVideoInfo.isPresent(), existVideoInfo.orElseGet(() -> null));
    }
}
