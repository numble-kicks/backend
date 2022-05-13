package numble.team4.shortformserver.video.integration;


import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.DELETE_VIDEO;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_BY_ID;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPLOAD_VIDEO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoRequest;
import numble.team4.shortformserver.video.dto.VideoResponse;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import numble.team4.shortformserver.video.ui.VideoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

@BaseIntegrationTest
public class VideoIntegrationTest {

    @Autowired
    private AmazonS3Uploader amazonS3Uploader;

    @Autowired
    private VideoController videoController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member author;
    private Member tester;
    private Video video;
    private Category category;
    private VideoUpdateRequest videoUpdateRequest;

    @BeforeEach
    void setUp() {
        category = categoryRepository.findByName("기타")
            .orElseThrow(NotFoundCategoryException::new);
        List<Member> members = List.of(
            createMember("author"),
            createMember("tester")
        );
        author = members.get(0);
        tester = members.get(1);

        memberRepository.saveAll(members);

        List<Video> videos = List.of(
            createVideo(1L, 3L),
            createVideo(5L, 0L),
            createVideo(3L, 0L),
            createVideo(4L, 0L),
            createVideo(100L, 0L)
        );

        videoRepository.saveAll(videos);

        video = videos.get(0);

        videoUpdateRequest = VideoUpdateRequest.builder()
            .title(video.getTitle())
            .description("설명 수정")
            .category("기타")
            .price(100)
            .usedStatus(false)
            .build();
    }

    @Nested
    @DisplayName("영상 등록 테스트")
    class UploadVideoTest {

        MockMultipartFile videoFile;
        MockMultipartFile thumbnailFile;
        VideoRequest videoRequest;

        @Test
        @DisplayName("영상 등록 성공")
        void uploadVideo_success() throws Exception {
            // given
            videoFile = new MockMultipartFile("video", "video".getBytes());
            thumbnailFile = new MockMultipartFile("thumbnail", "thumbnail".getBytes());

            videoRequest = new VideoRequest(videoFile, thumbnailFile, "제목", 100, false, "기타", "");

            // when
            CommonResponse<Long> response = videoController.saveVideo(
                videoRequest, author);

            Video savedVideo = videoRepository.findById(response.getData())
                .orElseThrow(NotExistVideoException::new);

            // then
            assertThat(response.getMessage()).isEqualTo(UPLOAD_VIDEO.getMessage());
            assertThat(savedVideo).isNotNull();

            amazonS3Uploader.deleteToS3(savedVideo.getVideoUrl());
            amazonS3Uploader.deleteToS3(savedVideo.getThumbnailUrl());
        }
    }

    @Nested
    @DisplayName("영상 상세정보 수정 테스트")
    class UpdateVideoTest {

        @Test
        @DisplayName("영상 수정 성공")
        void updateVideo_success() throws Exception {
            // when
            CommonResponse<Long> res = videoController.updateVideo(
                videoUpdateRequest, author, video.getId());

            Video savedVideo = videoRepository.findById(res.getData())
                .orElseThrow(NotExistVideoException::new);

            // then
            assertThat(savedVideo.getId()).isEqualTo(video.getId());
            assertThat(savedVideo.getDescription()).isEqualTo(videoUpdateRequest.getDescription());
        }

        @Test
        @DisplayName("영상 수정 실패, 작성자를 제외한 유저는 수정할 수 없다.")
        void updateVideo_notAuthor() throws Exception {
            // when, then
            assertThrows(NotAuthorException.class,
                () -> videoController.updateVideo(videoUpdateRequest, tester,
                    video.getId()));
        }

        @Test
        @DisplayName("영상 수정 실패, 존재하지 않는 영상은 수정할 수 없다.")
        void updateVideo_notExistVideo() throws Exception {
            // when, then
            assertThrows(NotExistVideoException.class,
                () -> videoController.updateVideo(videoUpdateRequest, author, 918367461L));
        }
    }

    @Nested
    @DisplayName("영상 삭제 테스트")
    class DeleteVideoTest {

        @Test
        @DisplayName("영상 삭제 성공")
        void deleteVideo_success() throws Exception {
            //given
            Long savedVideoId = videoController.saveVideo(new VideoRequest(
                new MockMultipartFile("video", "test".getBytes()),
                new MockMultipartFile("thumbnail", "test".getBytes()),
                "제목",
                10000,
                false,
                "기타",
                ""
            ), author).getData();

            // when
            CommonResponse<VideoResponse> res = videoController.deleteVideo(
                savedVideoId, author);

            // then
            assertThat(res.getMessage()).isEqualTo(DELETE_VIDEO.getMessage());
            assertThat(videoRepository.existsById(savedVideoId)).isFalse();
        }

        @Test
        @DisplayName("영상 삭제 실패, 작성자를 제외한 유저는 삭제할 수 없다.")
        void deleteVideo_notAuthor() throws Exception {
            assertThrows(NotAuthorException.class,
                () -> videoController.deleteVideo(video.getId(), tester));
        }

        @Test
        @DisplayName("영삭 삭제 실패, 존재하지 않는 영상은 삭제할 수 없다.")
        void deleteVideo_notExistVideo() throws Exception {
            assertThrows(NotExistVideoException.class,
                () -> videoController.deleteVideo(100L, author));
        }
    }

    @Nested
    @DisplayName("영상 조회 테스트")
    class GetVideoTest {

        private static final String HITS = "hits";
        private static final String LIKES = "likes";

        @Test
        @DisplayName("특정 영상 조회 성공")
        void findById() throws Exception {
            // given
            Long viewCount = video.getViewCount();

            // when
            CommonResponse<VideoResponse> response = videoController.findById(video.getId());
            VideoResponse data = response.getData();

            // then
            assertThat(response.getMessage()).isEqualTo(GET_VIDEO_BY_ID.getMessage());
            assertThat(viewCount + 1).isEqualTo(data.getViewCount());
        }

        @Test
        @DisplayName("특정 영상 조회 실패, 존재하지 않는 영상은 조회할 수 없다.")
        void findById_notExistVideo() throws Exception {
            assertThrows(NotExistVideoException.class, () -> videoController.findById(100L));
        }
    }

    private Member createMember(String name) {
        return Member.builder()
            .name(name)
            .role(MEMBER)
            .build();
    }

    private Video createVideo(long likeCount, long viewCount) {
        return Video.builder()
            .title("제목")
            .description("설명")
            .price(10000)
            .category(category)
            .usedStatus(false)
            .videoUrl("video URL")
            .thumbnailUrl("thumbnail URL")
            .member(author)
            .viewCount(viewCount)
            .likeCount(likeCount)
            .build();
    }


}
