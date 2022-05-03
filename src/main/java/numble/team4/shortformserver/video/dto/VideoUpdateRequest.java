package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Valid
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoUpdateRequest {

    private String category;

    @NonNull
    private String title;

    private String description;

    public Video toVideo() {
        return Video.builder()
            .title(title)
            .description(description)
            .build();
    }
}
