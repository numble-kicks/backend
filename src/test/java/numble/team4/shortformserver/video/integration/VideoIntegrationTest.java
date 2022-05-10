package numble.team4.shortformserver.video.integration;


import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.DELETE_VIDEO;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.GET_VIDEO_BY_ID;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPLOAD_VIDEO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.common.dto.PageInfo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
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
import org.springframework.data.domain.PageRequest;
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

    private Member author;
    private Member tester;
    private Video video;

    @BeforeEach
    void setUp() {
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
            createVideo(100, 0L)
        );

        videoRepository.saveAll(videos);

        for (Video v : videos) {
            v.addToMember(author);
        }

        video = videos.get(0);
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

            videoRequest = new VideoRequest(videoFile, thumbnailFile, "제목", null, "설명");

            // when
            CommonResponse<VideoResponse> response = videoController.saveVideo(
                videoRequest, author);

            VideoResponse videoResponse = response.getData();
            // then
            assertThat(response.getMessage()).isEqualTo(UPLOAD_VIDEO.getMessage());
            assertThat(videoResponse).isNotNull();
            assertThat(author.getVideos()).hasSize(6);

            amazonS3Uploader.deleteToS3(videoResponse.getVideoUrl());
            amazonS3Uploader.deleteToS3(videoResponse.getThumbnailUrl());
        }

        @Test
        @DisplayName("영상 등록 실패, 회원이 아니면 영상들 등록할 수 없다.")
        void uploadVideo_NotExistMember() throws Exception {
            // given
            Member guest = Member.builder()
                .id(100L)
                .role(MEMBER)
                .name("guest")
                .build();
            videoFile = new MockMultipartFile("video", "video".getBytes());
            thumbnailFile = new MockMultipartFile("thumbnail", "thumbnail".getBytes());

            videoRequest = new VideoRequest(videoFile, thumbnailFile, "제목", null, "설명");

            // when, then
            assertThrows(NotExistMemberException.class,
                () -> videoController.saveVideo(videoRequest, guest));
        }
    }

    @Nested
    @DisplayName("영상 상세정보 수정 테스트")
    class UpdateVideoTest {

        VideoUpdateRequest videoUpdateRequest;

        @Test
        @DisplayName("영상 수정 성공")
        void updateVideo_success() throws Exception {
            // given
            videoUpdateRequest = VideoUpdateRequest.builder()
                .title(video.getTitle())
                .description("설명 수정")
                .build();

            // when
            CommonResponse<VideoResponse> response = videoController.updateVideo(
                videoUpdateRequest, author, video.getId());
            VideoResponse data = response.getData();

            // then
            assertThat(data.getId()).isEqualTo(video.getId());
            assertThat(data.getDescription()).isEqualTo(videoUpdateRequest.getDescription());
        }

        @Test
        @DisplayName("영상 수정 실패, 작성자를 제외한 유저는 수정할 수 없다.")
        void updateVideo_notAuthor() throws Exception {
            // given
            videoUpdateRequest = VideoUpdateRequest.builder()
                .title(video.getTitle())
                .description("설명 수정")
                .build();
            // when, then
            assertThrows(NotAuthorException.class,
                () -> videoController.updateVideo(videoUpdateRequest, tester,
                    video.getId()));
        }

        @Test
        @DisplayName("영상 수정 실패, 존재하지 않는 영상은 수정할 수 없다.")
        void updateVideo_notExistVideo() throws Exception {
            // given
            videoUpdateRequest = VideoUpdateRequest.builder()
                .title(video.getTitle())
                .description("설명 수정")
                .build();

            // when, then
            assertThrows(NotExistVideoException.class,
                () -> videoController.updateVideo(videoUpdateRequest, author, 100L));
        }
    }

    @Nested
    @DisplayName("영상 삭제 테스트")
    class DeleteVideoTest {

        @Test
        @DisplayName("영상 삭제 성공")
        void deleteVideo_success() throws Exception {
            //given
            VideoResponse savedVideo = videoController.saveVideo(new VideoRequest(
                new MockMultipartFile("test", "test".getBytes()),
                new MockMultipartFile("test", "test".getBytes()),
                "제목",
                "",
                "설명"
            ), author).getData();

            // when
            CommonResponse<VideoResponse> res = videoController.deleteVideo(
                savedVideo.getId(), author);

            // then
            assertThat(res.getMessage()).isEqualTo(DELETE_VIDEO.getMessage());
            assertThat(author.getVideos()).hasSize(5);
            assertThat(videoRepository.existsById(savedVideo.getId())).isFalse();
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
            CommonResponse<VideoResponse> response = videoController.findByIdVideo(video.getId());
            VideoResponse data = response.getData();

            // then
            assertThat(response.getMessage()).isEqualTo(GET_VIDEO_BY_ID.getMessage());
            assertThat(viewCount + 1).isEqualTo(data.getViewCount());
        }

        @Test
        @DisplayName("특정 영상 조회 실패, 존재하지 않는 영상은 조회할 수 없다.")
        void findById_notExistVideo() throws Exception {
            assertThrows(NotExistVideoException.class, () -> videoController.findByIdVideo(100L));
        }

        @Test
        @DisplayName("영상 목록 조회 - 조회순으로 정렬")
        void findVideoSortByHits() throws Exception {
            // when
            CommonResponse<List<VideoResponse>> allVideos = videoController.findAllVideos(HITS,
                null, PageRequest.of(0, 5));

            List<VideoResponse> data = allVideos.getData();

            // then
            assertThat(data).hasSize(5);
            assertThat(data)
                .extracting("viewCount")
                .containsExactly(3L, 0L, 0L, 0L, 0L);
        }

        @Test
        @DisplayName("영상 목록 조회 - 좋아요 순으로 정렬 및 pagination 테스트")
        void findVideoSortByLikes() throws Exception {
            // given
            List<Video> all = videoRepository.findAll();
            List<Long> ids = new ArrayList<>();

            for (Video v : all) {
                ids.add(v.getId());
            }

            // when
            List<VideoResponse> data1 = videoController.findAllVideos(LIKES, null,
                PageRequest.of(0, 5)).getData();

            List<VideoResponse> data2 = videoController.findAllVideos(LIKES, ids.get(4),
                PageRequest.of(0, 3)).getData();

            List<VideoResponse> data3 = videoController.findAllVideos(LIKES, ids.get(1),
                PageRequest.of(0, 3)).getData();

            // then
            assertThat(data1)
                .extracting("id")
                .containsExactly(ids.get(4), ids.get(1), ids.get(3), ids.get(2), ids.get(0));

            assertThat(data2)
                .extracting("id")
                .containsExactly(ids.get(1), ids.get(3), ids.get(2));

            assertThat(data3)
                .extracting("id")
                .containsExactly(ids.get(3), ids.get(2), ids.get(0));
        }

        @Test
        @DisplayName("특정 사용자의 영상 목록 조회")
        void findAllVideosOfMember() throws Exception {
            // when
            CommonResponse<List<VideoResponse>> response = videoController.findAllVideosOfMember(
                author.getId(), null, PageRequest.of(0, 3));
            PageInfo pageInfo = response.getPageInfo();

            List<VideoResponse> data = response.getData();

            List<VideoResponse> data1 = videoController.findAllVideosOfMember(tester.getId(), null,
                PageRequest.of(0, 3)).getData();

            // then
            assertThat(data1).hasSize(0);
            assertThat(pageInfo.getTotalPages()).isEqualTo(2);
            assertThat(data)
                .extracting("member")
                .extracting("name")
                .containsExactly("author", "author", "author");
        }

        @Test
        @DisplayName("특정 사용자의 영상 목록 조회 실패, 존재하지 않는 사용자의 영상 목록은 조회할 수 없다.")
        void findAllVideosOfMember_notExistMember() {
            assertThrows(NotExistMemberException.class,
                () -> videoController.findAllVideosOfMember(100L, null, PageRequest.of(0, 10)));
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
            .videoUrl("video URL")
            .thumbnailUrl("thumbnail URL")
            .member(author)
            .viewCount(viewCount)
            .likeCount(likeCount)
            .build();
    }


}
