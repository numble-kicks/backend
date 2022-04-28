package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Builder
public class VideoRequest {

    private String title;
    private String description;

    public Video toVideo(String videoUrl, String thumbnailUrl, Member member) {
        return Video
            .builder()
            .title(title)
            .description(description)
            .videoUrl(videoUrl)
            .likeCount(0L)
            .viewCount(0L)
            .thumbnailUrl(thumbnailUrl)
            .member(member)
            .build();
    }
}
