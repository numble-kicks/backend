package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Comments;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@Valid
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoResponse {

    @NonNull
    private Long id;

    @NonNull
    private String title;

    private String description;

    @NonNull
    private String videoUrl;

    @NonNull
    private String thumbnailUrl;

    @NonNull
    private Long viewCount;

    @NonNull
    private Long likeCount;

    @NonNull
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
