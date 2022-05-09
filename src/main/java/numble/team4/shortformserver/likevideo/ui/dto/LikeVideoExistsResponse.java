package numble.team4.shortformserver.likevideo.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeVideoExistsResponse {

    private boolean isExistLikeVideo;

    public static LikeVideoExistsResponse from(boolean likeVideoExists) {
        return new LikeVideoExistsResponse(likeVideoExists);
    }
}
