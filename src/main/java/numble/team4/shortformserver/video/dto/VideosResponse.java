package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class VideosResponse {

    private Long id;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    public static VideosResponse from(Video video) {
        return VideosResponse.builder()
            .id(video.getId())
            .thumbnailUrl(video.getThumbnailUrl())
            .build();
    }

    public static List<VideosResponse> from(List<Video> videos) {
        return videos.stream()
            .map(VideosResponse::from)
            .collect(Collectors.toList());
    }
}
