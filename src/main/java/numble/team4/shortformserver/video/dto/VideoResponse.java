package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private Long id;
    private String title;
    private String description;
    @JsonProperty("video_url")
    private String videoUrl;
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    private Integer price;
    @JsonProperty("view_count")
    private Long viewCount;
    @JsonProperty("like_count")
    private Long likeCount;
    private MemberDto member;
    private CategoryDto category;
    @JsonProperty("used_status")
    private Boolean usedStatus;

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
            .category(CategoryDto.from(video.getCategory()))
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
