package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.dto.MemberInfoResponse;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoResponse {

    private Long id;
    private String category;
    private String title;
    private String description;
    private Integer price;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    @JsonProperty("video_url")
    private String videoUrl;
    @JsonProperty("view_count")
    private Long viewCount;
    @JsonProperty("like_count")
    private Long likeCount;
    @JsonProperty("used_status")
    private Boolean usedStatus;
    private MemberInfoResponse user;

    public static VideoResponse from(Video video) {
        return VideoResponse.builder()
            .id(video.getId())
            .title(video.getTitle())
            .description(video.getDescription())
            .price(video.getPrice())
            .usedStatus(video.getUsedStatus())
            .videoUrl(video.getVideoUrl())
            .thumbnailUrl(video.getThumbnailUrl())
            .viewCount(video.getViewCount())
            .likeCount(video.getLikeCount())
            .category(video.getCategory().getName())
            .user(MemberInfoResponse.from(video.getMember()))
            .build();
    }
}
