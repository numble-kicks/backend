package numble.team4.shortformserver.video.integration;


import static numble.team4.shortformserver.member.member.domain.Role.ADMIN;
import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NoAccessPermissionException;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideosResponse;
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
            createVideo(10L, "우르오스", "공통 우르오스"),
            createVideo(5L, "나이키", "공통 나이키 신발"),
            createVideo(5L, "범고래", "공통 나이키 덩크로우 범고래, 드로우 신발"),
            createVideo(3L, "뉴발 992", "공통 뉴발란스 992 신발"),
            createVideo(3L, "에어맥스", "공통 나이키 에어맥스95 신발"),
            createVideo(1L, "아이폰", "공통 애플")
        );

        videoRepository.saveAll(videos);

        video = videos.get(0);

        videoUpdateRequest = VideoUpdateRequest.builder()
            .title(video.getTitle())
            .description("설명 수정")
            .category("기타")
            .price(100)
            .used_status(false)
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
        void uploadVideo_success() {
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
        void updateVideo_success() {
            // when
            CommonResponse<Long> res = videoController.updateVideo(
                videoUpdateRequest, author, video.getId());

            // then
            assertThat(video.getDescription()).isEqualTo(videoUpdateRequest.getDescription());
        }

        @Test
        @DisplayName("영상 수정 실패, 작성자를 제외한 유저는 수정할 수 없다.")
        void updateVideo_notAuthor() {
            // given
            Long videoId = video.getId();

            // when, then
            assertThrows(NoAccessPermissionException.class,
                () -> videoController.updateVideo(videoUpdateRequest, tester,
                    videoId));
        }

        @Test
        @DisplayName("영상 수정 실패, 존재하지 않는 영상은 수정할 수 없다.")
        void updateVideo_notExistVideo()  {
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
        void deleteVideo_success() {
            // given
            Long videoId = video.getId();

            // when
            CommonResponse<VideoResponse> res = videoController.deleteVideo(videoId, author);

            // then
            assertThat(res.getMessage()).isEqualTo(DELETE_VIDEO.getMessage());
            assertThat(videoRepository.existsById(videoId)).isFalse();
        }

        @Test
        @DisplayName("영상 삭제 실패, 작성자를 제외한 유저는 삭제할 수 없다.")
        void deleteVideo_notAuthor()  {
            // given
            Long videoId = video.getId();

            // when, then
            assertThrows(NoAccessPermissionException.class,
                () -> videoController.deleteVideo(videoId, tester));
        }

        @Test
        @DisplayName("영삭 삭제 실패, 존재하지 않는 영상은 삭제할 수 없다.")
        void deleteVideo_notExistVideo()  {
            assertThrows(NotExistVideoException.class,
                () -> videoController.deleteVideo(1234567890L, author));
        }
    }

    @Nested
    @DisplayName("영상 조회 테스트")
    class GetVideoTest {
        @Test
        @DisplayName("특정 영상 조회 실패, 존재하지 않는 영상은 조회할 수 없다.")
        void findById_notExistVideo()  {
            assertThrows(NotExistVideoException.class, () -> videoController.findVideoById(100L));
        }

        @Test
        @DisplayName("모든 영상 조회")
        void getAllVideo() {
            // given
            int size = videoRepository.findAll().size();

            // when
            List<VideosResponse> all = videoController.getAllVideos().getData();

            // then
            assertThat(all).hasSize(size);
        }
    }

    @Nested
    @DisplayName("관리자 권한 테스트")
    class AdminPermissionTest {

        @Test
        @DisplayName("관리자가 영상을 수정한다.")
        void updateVideoByAdmin() {
            // given
            Member admin = Member.builder()
                .role(ADMIN)
                .email("admin@test.com")
                .build();
            memberRepository.save(admin);
            Long videoId = video.getId();

            // when
            CommonResponse<Long> res = videoController.updateVideo(
                videoUpdateRequest, admin, videoId);

            // then
            assertThat(res.getMessage()).isEqualTo(UPDATE_VIDEO.getMessage());
        }

        @Test
        @DisplayName("관리자가 영상을 식제한다.")
        void deleteVideoByAdmin() {
            // given
            Member admin = Member.builder()
                .role(ADMIN)
                .email("admin@test.com")
                .build();
            memberRepository.save(admin);
            Long videoId = video.getId();

            // when
            CommonResponse<VideoResponse> res = videoController.deleteVideo(videoId, admin);

            // then
            assertThat(res.getMessage()).isEqualTo(DELETE_VIDEO.getMessage());
        }
    }


    private Member createMember(String name) {
        return Member.builder()
            .name(name)
            .role(MEMBER)
            .build();
    }

    private Video createVideo(long viewCount, String title, String description) {
        return Video.builder()
            .title(title)
            .description(description)
            .price(10000)
            .category(category)
            .usedStatus(false)
            .videoUrl("video URL")
            .thumbnailUrl("thumbnail URL")
            .member(author)
            .viewCount(viewCount)
            .build();
    }
}
