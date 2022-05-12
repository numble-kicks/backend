package numble.team4.shortformserver.video.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Optional;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

        videoRequest = new VideoRequest(videos, thumbnail, "title", "category", "description");

        member = Member.builder()
            .id(10L)
            .email("author@test.com")
            .videos(new ArrayList<>())
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

    @Nested
    @DisplayName("Video 저장 테스트")
    class uploadVideoTest {

        @Test
        @DisplayName("video 저장 - 성공")
        void uploadVideo_success() throws Exception {
            // given
            given(amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video")).willReturn(videoDto);
            given(amazonS3Uploader.saveToS3(videoRequest.getThumbnail(), "video/thumbnail")).willReturn(thumbnailDto);
            given(videoRepository.save(any(Video.class))).willReturn(video);

            // when
            VideoResponse savedVideo = videoService.uploadVideo(videoRequest, member);

            // then
            assertThat(VideoResponse.from(video).getId()).isEqualTo(savedVideo.getId());
        }

        @Test
        @DisplayName("video 저장 - 실패, 파일이 존재하지 않을 경우")
        void uploadVideo_notExistFile() throws Exception {
            // given
            given(amazonS3Uploader.saveToS3(videoRequest.getVideo(), "video")).willThrow(NotExistFileException.class);

            // then
            assertThrows(NotExistFileException.class,
                () -> videoService.uploadVideo(videoRequest, member));
        }
    }

    @Nested
    @DisplayName("Video 수정 테스트")
    class updateVideoTest {

        @Test
        @DisplayName("video 수정 - 성공")
        void updateVideo_success() throws Exception {
            // given
            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
                .title("update title")
                .description("")
                .build();

            given(videoRepository.findById(video.getId())).willReturn(Optional.of(video));

            // when
            VideoResponse videoResponse = videoService.updateVideo(videoUpdateRequest, member,
                video.getId());

            // then
            assertThat(videoResponse.getId()).isEqualTo(video.getId());
            assertThat(videoResponse.getTitle()).isEqualTo(videoUpdateRequest.getTitle());
            assertThat(videoResponse.getDescription()).isEqualTo(
                videoUpdateRequest.getDescription());
        }

        @Test
        @DisplayName("video 수정 - 실패, 작성자가 아닌 회원이 수정을 시도할 경우")
        void updateVideo_notAuthor() throws Exception {
            // given
            Member notAuthor = Member.builder()
                .id(100L)
                .email("notAuthor@test.com")
                .build();
            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder().title("t")
                .description("").build();

            given(videoRepository.findById(anyLong())).willReturn(Optional.of(video));

            // when, then
            assertThrows(NotAuthorException.class,
                () -> videoService.updateVideo(videoUpdateRequest, notAuthor, anyLong()));
        }

        @Test
        @DisplayName("video 수정 - 실패, 존재하지 않는 영상을 수정할 경우")
        void updateVideo_notExistVideo() throws Exception {
            assertThrows(NotExistVideoException.class,
                () -> videoService.updateVideo(any(), member, 1L));
        }
    }

    @Nested
    @DisplayName("Video 삭제 테스트")
    class DeleteVideo {

        @Test
        @DisplayName("Video 삭제 - 성공")
        void deleteVideo_success() throws Exception {
            // given
            given(videoRepository.findById(anyLong())).willReturn(Optional.of(video));

            // when
            videoService.deleteVideo(video.getId(), member);

            // then
            assertThat(member.getVideos()).isEmpty();
        }

        @Test
        @DisplayName("Video 삭제 - 실패, 작성자가 아닌 다른 유저가 삭제를 시도할 경우")
        void deleteVideo_notAuthor() throws Exception {
            // given
            Member otherMember = Member.builder()
                .id(100L)
                .email("otherMember@test.com")
                .build();

            given(videoRepository.findById(anyLong())).willReturn(Optional.of(video));

            // when, then
            assertThrows(NotAuthorException.class,
                () -> videoService.deleteVideo(video.getId(), otherMember));
        }

        @Test
        @DisplayName("Video 삭제 - 실패, 존재하지 않는 영상을 삭제할 때")
        void deleteVideo_notExistVideo() throws Exception {
            assertThrows(NotExistVideoException.class,
                () -> videoService.deleteVideo(100L, member));
        }
    }


}