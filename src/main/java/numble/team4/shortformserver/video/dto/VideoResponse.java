package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String videoUrl;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private Long viewCount;

    @NotNull
    private Long likeCount;

    @NotNull
    private MemberDto member;

    public static VideoResponse from(Video video) {
        return VideoResponse.builder()
            .id(video.getId())
            .title(video.getTitle())
            .description(video.getDescription())
            .videoUrl(video.getVideoUrl())
            .thumbnailUrl(video.getThumbnailUrl())
            .viewCount(video.getViewCount())
            .likeCount(video.getLikeCount())
            .member(MemberDto.from(video.getMember().getName()))
            .build();
    }

    @AllArgsConstructor(access = PRIVATE)
    static class MemberDto {
        private String name;

        private static MemberDto from(String name) {
            return new MemberDto(name);
        }
    }
}
