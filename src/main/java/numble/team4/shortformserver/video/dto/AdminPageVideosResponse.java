package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.dto.MemberInfoResponseForVideo;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class AdminPageVideosResponse {

    private Long id;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    private String title;

    private MemberInfoResponseForVideo user;

    public static AdminPageVideosResponse from(Video video) {
        return AdminPageVideosResponse.builder()
            .id(video.getId())
            .thumbnailUrl(video.getThumbnailUrl())
            .title(video.getTitle())
            .user(MemberInfoResponseForVideo.from(video.getMember()))
            .build();
    }

}
