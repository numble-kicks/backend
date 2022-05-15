package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
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
    private MemberDto user;

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
            .user(MemberDto.from(video.getMember()))
            .build();
    }

    @AllArgsConstructor(access = PRIVATE)
    @Getter
    static class MemberDto {
        private Long id;

        private String name;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        private static MemberDto from(Member member) {
            return new MemberDto(member.getId(), member.getName(), member.getProfileImageUrl());
        }
    }
}
