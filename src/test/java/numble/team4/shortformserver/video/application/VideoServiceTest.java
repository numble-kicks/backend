package numble.team4.shortformserver.video.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    VideoRepository videoRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    AmazonS3Uploader amazonS3Uploader;

    @InjectMocks
    VideoService videoService;

    private VideoRequest videoRequest;
    private Member member;
    private MockMultipartFile videos;
    private MockMultipartFile thumbnail;

    @BeforeEach
    void setUp() {
        videoRequest = VideoRequest
            .builder()
            .title("title")
            .description("description")
            .build();
        member = Member.builder()
            .id(10L)
            .build();
        videos = new MockMultipartFile("test", new byte[]{});
        thumbnail = new MockMultipartFile("test", new byte[]{});
    }

    @Test
    @DisplayName("video 저장 - 성공")
    public void uploadVideo_success() throws Exception {
        // given
        S3UploadDto videoDto = new S3UploadDto("test.mov",
            "https://d659rm6fgd091.cloudfront.net/test.mov");
        S3UploadDto thumbnailDto = new S3UploadDto("test.mov",
            "https://d659rm6fgd091.cloudfront.net/test.mov");

        Video video = Video.builder()
            .title(videoRequest.getTitle())
            .description(videoRequest.getDescription())
            .videoUrl(videoDto.getFileUrl())
            .thumbnailUrl(thumbnailDto.getFileUrl())
            .member(member)
            .viewCount(0L)
            .likeCount(0L)
            .build();

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(amazonS3Uploader.saveToS3(videos, "video")).willReturn(videoDto);
        given(amazonS3Uploader.saveToS3(thumbnail, "video/thumbnail")).willReturn(thumbnailDto);
        given(videoRepository.save(any())).willReturn(video);

        // when
        Video uploadedVideo = videoService.upload(videoRequest, member, videos, thumbnail);

        // then
        assertThat(video).isEqualTo(uploadedVideo);
    }

    @Test
    @DisplayName("video 저장 - 실패, 회원이 존재하지 않음")
    public void uploadVideo_notExistMember() throws Exception {
        // then
        assertThrows(NotExistMemberException.class,
            () -> videoService.upload(videoRequest, member, videos, thumbnail));
    }

    @Test
    @DisplayName("video 저장 - 실패, 파일이 존재하지 않음")
    public void uploadVideo_notExistFile() throws Exception {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(amazonS3Uploader.saveToS3(videos, "video")).willThrow(NotExistFileException.class);

        // then
        assertThrows(NotExistFileException.class,
            () -> videoService.upload(videoRequest, member, videos, thumbnail));
    }
}