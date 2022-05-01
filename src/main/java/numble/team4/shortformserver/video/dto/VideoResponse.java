package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Comments;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoResponse {

    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long viewCount;
    private Long likeCount;
    private Member member;
    private Comments comments;

    public static VideoResponse of(Video video) {
        return VideoResponse.builder()
            .id(video.getId())
            .title(video.getTitle())
            .description(video.getDescription())
            .videoUrl(video.getVideoUrl())
            .thumbnailUrl(video.getThumbnailUrl())
            .viewCount(video.getViewCount())
            .likeCount(video.getLikeCount())
            .build();
    }
}
