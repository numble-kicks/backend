package numble.team4.shortformserver.likevideo.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeVideoExistResponse {

    private boolean existLikeVideo;

    public static LikeVideoExistResponse from(boolean likeVideoExists) {
        return new LikeVideoExistResponse(likeVideoExists);
    }
}
