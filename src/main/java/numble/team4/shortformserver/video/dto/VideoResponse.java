package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class VideoResponse {

    private Long id;
    private String title;
    private String description;
    private String video_url;
    private String thumbnail_url;
    private Long view_count;
    private Long like_count;
    private MemberDto member;

    public static VideoResponse from(Video video) {
        return VideoResponse.builder()
            .id(video.getId())
            .title(video.getTitle())
            .description(video.getDescription())
            .video_url(video.getVideoUrl())
            .thumbnail_url(video.getThumbnailUrl())
            .view_count(video.getViewCount())
            .like_count(video.getLikeCount())
            .member(MemberDto.from(video.getMember().getName()))
            .build();
    }

    @AllArgsConstructor(access = PRIVATE)
    @Getter
    static class MemberDto {
        private String name;

        private static MemberDto from(String name) {
            return new MemberDto(name);
        }
    }
}
