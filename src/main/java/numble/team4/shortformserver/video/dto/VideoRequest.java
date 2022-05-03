package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.*;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PROTECTED)
@Builder
@Valid
public class VideoRequest {

    @NonNull
    private MultipartFile video;
    @NonNull
    private MultipartFile thumbnail;

    @NonNull
    private String title;

    private String category;
    private String description;

    public Video toVideo(String videoUrl, String thumbnailUrl, Member member) {
        return Video.builder()
            .title(title)
            .description(description)
            .videoUrl(videoUrl)
            .thumbnailUrl(thumbnailUrl)
            .likeCount(0L)
            .viewCount(0L)
            .member(member)
            .build();
    }
}
