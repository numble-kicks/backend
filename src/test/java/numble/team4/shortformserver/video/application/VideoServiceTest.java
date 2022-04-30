package numble.team4.shortformserver.video.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
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

    private Member member;
    private VideoRequest videoRequest;
    private Video video;

    private S3UploadDto videoDto;
    private S3UploadDto thumbnailDto;


    @BeforeEach
    void setUp() {
        videoDto = new S3UploadDto("test.mov",
            "https://d659rm6fgd091.cloudfront.net/test.mov");
        thumbnailDto = new S3UploadDto("test.mov",
            "https://d659rm6fgd091.cloudfront.net/test.mov");

        MockMultipartFile videos = new MockMultipartFile("test", new byte[]{});
        MockMultipartFile thumbnail = new MockMultipartFile("test", new byte[]{});

        videoRequest = VideoRequest
            .builder()
            .title("title")
            .description("description")
            .video(videos)
            .thumbnail(thumbnail)
            .build();

        member = Member.builder()
            .id(10L)
            .build();

        video = Video.builder()
            .id(10L)
            .title(videoRequest.getTitle())
            .description(videoRequest.getDescription())
            .videoUrl(videoDto.getFileUrl())
            .thumbnailUrl(thumbnailDto.getFileUrl())
            .member(member)
            .viewCount(0L)
            .likeCount(0L)
            .build();
    }

    //== video 저장 테스트 ==//
    @Test
    @DisplayName("video 저장 - 성공")
    public void uploadVideo_success() throws Exception {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video")).willReturn(videoDto);
        given(amazonS3Uploader.saveToS3(videoRequest.getThumbnail(), "video/thumbnail")).willReturn(thumbnailDto);
        given(videoRepository.save(any())).willReturn(video);

        // when
        Video uploadedVideo = videoService.uploadVideo(videoRequest, member);

        // then
        assertThat(video).isEqualTo(uploadedVideo);
    }

    @Test
    @DisplayName("video 저장 - 실패, 회원이 존재하지 않을 경우")
    public void uploadVideo_notExistMember() throws Exception {
        // then
        assertThrows(NotExistMemberException.class,
            () -> videoService.uploadVideo(videoRequest, member));
    }

    @Test
    @DisplayName("video 저장 - 실패, 파일이 존재하지 않을 경우")
    public void uploadVideo_notExistFile() throws Exception {
        // given
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video")).willThrow(NotExistFileException.class);

        // then
        assertThrows(NotExistFileException.class,
            () -> videoService.uploadVideo(videoRequest, member));
    }

    //== video 수정 테스트 ==//
    @Test
    @DisplayName("video 수정 - 성공")
    void updateVideo_success() throws Exception {
        // given
        VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
            .title("update title")
            .description("")
            .build();

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(videoRepository.findById(video.getId())).willReturn(Optional.of(video));

        // when
        Video updatedVideo = videoService.updateVideo(videoUpdateRequest, member, this.video.getId());

        // then
        assertThat(updatedVideo.getId()).isEqualTo(video.getId());
        assertThat(updatedVideo.getTitle()).isEqualTo(videoUpdateRequest.getTitle());
        assertThat(updatedVideo.getDescription()).isEqualTo(videoUpdateRequest.getDescription());
    }

    @Test
    @DisplayName("video 수정 - 실패, 작성자가 아닌 회원이 수정을 시도할 경우")
    void updateVideo_notAuthor() throws Exception {
        // given
        Member notAuthor = Member.builder()
            .id(100L)
            .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(notAuthor));
        given(videoRepository.findById(video.getId())).willReturn(Optional.of(video));

        // when, then
        assertThrows(NotAuthorException.class,
            () -> videoService.updateVideo(any(), notAuthor, video.getId()));
    }

    @Test
    @DisplayName("video 수정 - 실패, 저장한 영상이 존재하지 않을 경우")
    void updateVideo_notExistVideo() throws Exception {
        assertThrows(NotExistVideoException.class,
            () -> videoService.updateVideo(any(), member, 1L));
    }
}