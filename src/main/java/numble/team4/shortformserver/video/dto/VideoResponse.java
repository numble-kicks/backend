package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.video.category.dto.CategoryDto;
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

    @NotBlank
    private String videoUrl;

    @NotBlank
    private String thumbnailUrl;

    @NotNull
    private Long price;

    @NotNull
    private Long viewCount;

    @NotNull
    private Long likeCount;

    @NotNull
    private MemberDto member;

    private CategoryDto category;
    private Boolean usedStatus;
    private String description;

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
            .category(CategoryDto.create(video.getCategory()))
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
