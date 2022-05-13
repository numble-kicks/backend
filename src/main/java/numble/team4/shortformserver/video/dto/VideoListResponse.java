package numble.team4.shortformserver.video.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import numble.team4.shortformserver.video.domain.Video;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VideoListResponse {

    private Long id;
    private String thumbnailUrl;

    private static VideoListResponse from(Video video) {
        return new VideoListResponse(video.getId(), video.getThumbnailUrl());
    }

    public static List<VideoListResponse> from(List<Video> videos) {
        return videos.stream()
                .map(VideoListResponse::from)
                .collect(Collectors.toList());
    }
}
