package numble.team4.shortformserver.video.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VideoListRequest {

    @NotNull(message = "정렬 방식은 필수 입니다.")
    private String sortBy;

    private Integer limitNum;
}
