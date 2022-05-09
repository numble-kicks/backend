package numble.team4.shortformserver.video.dto;

import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor(access = PUBLIC)
@NoArgsConstructor(access = PROTECTED)
public class VideoRequest {

    @NotNull(message = "영상은 null 일 수 없습니다.")
    private MultipartFile video;

    @NotNull(message = "썸네일은 null 일 수 없습니다.")
    private MultipartFile thumbnail;

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    private String title;

    private String category;
    private String description;

    public Video toVideo(String videoUrl, String thumbnailUrl, Member member) {
        Video video = Video.builder()
            .title(title)
            .description(description)
            .videoUrl(videoUrl)
            .thumbnailUrl(thumbnailUrl)
            .likeCount(0L)
            .viewCount(0L)
            .member(member)
            .build();

        video.addToMember(member);
        return video;
    }
}
