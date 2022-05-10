package numble.team4.shortformserver.video.acceptance;

import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPLOAD_VIDEO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.testCommon.mockUser.WithMockCustomUser;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

@WithMockCustomUser(name = "numble", email = "numble@numble.com")
public class VideoAcceptanceTest extends BaseAcceptanceTest {

    private static final String TITLE = "제목";
    private static final String DESCRIPTION = "설명";
    private static final String VIDEO_URL = "https://video.com";
    private static final String THUMBNAIL_URL = "https://thumbnail.com";
    private static final MockMultipartFile VIDEO_FILE = new MockMultipartFile("video",
        "범고래".getBytes());
    private static final MockMultipartFile THUMBNAIL_FILE = new MockMultipartFile("thumbnail",
        "범고래".getBytes());
    private static final String BASE_URI = "/v1/videos";
    private static final String VIDEO_ID = "/{videoId}";

    @Autowired
    AmazonS3Uploader amazonS3Uploader;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    VideoRepository videoRepository;

    Member user1, user2;

    @BeforeEach
    void setUp() {
        user1 = getUser("numble@numble.com");
        user2 = Member.builder()
            .name("tester")
            .build();

        memberRepository.save(user2);
    }

    @DisplayName("user1은 영상을 등록하고 자신의 영상 정보를 조회한다.")
    @Test
    void scenario1() throws Exception {
        // when
        ResultActions res = saveVideo();

        // then
        assertThat(user1.getVideos()).hasSize(1);

        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(UPLOAD_VIDEO.getMessage()))
            .andDo(print());

        // given - 영상 조회
        Video video = user1.getVideos().get(0);

        // when
        ResultActions read = mockMvc.perform(get(BASE_URI + VIDEO_ID, video.getId()));

        // then
        read.andExpect(status().isOk())
            .andDo(print());

        assertThat(video.getViewCount()).isEqualTo(1L);

        amazonS3Uploader.deleteToS3(video.getVideoUrl());
        amazonS3Uploader.deleteToS3(video.getThumbnailUrl());
    }

    @Test
    @DisplayName("user1은 저장한 영상을 수정한다.")
    void scenario2() throws Exception {
        // given
        Video video = createVideo(0L, 0L, user1);
        Video savedVideo = videoRepository.save(video);
        VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
            .title(TITLE)
            .description("설명 수정")
            .build();

        // when
        ResultActions res = mockMvc.perform(put(BASE_URI + VIDEO_ID, savedVideo.getId())
            .content(objectMapper.writeValueAsString(videoUpdateRequest))
            .contentType(APPLICATION_JSON)
        );

        // then
        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.description").value(videoUpdateRequest.getDescription()))
            .andDo(print());
    }

    @Test
    @DisplayName("user1은 영상 두 개를 저장하고, 마지막에 등록한 영상을 삭제한다.")
    void scenario3() throws Exception {
        // given
        saveVideo();
        saveVideo();

        // 두 개의 영상이 정상적으로 등록 됐는지 확인
        assertThat(user1.getVideos()).hasSize(2);

        Video video = user1.getVideos().get(1);

        // when
        ResultActions res = mockMvc.perform(delete(BASE_URI + VIDEO_ID, video.getId()));

        // then
        res.andExpect(status().isOk())
            .andDo(print());

        assertThat(user1.getVideos()).hasSize(1);
    }

    @Test
    @DisplayName("user1가 user2의 영상을 수정 시도한다. - 401 에러")
    void scenario4() throws Exception {
        // given
        Video savedVideo = videoRepository.save(createVideo(0L, 0L, user2));

        // when
        ResultActions res = mockMvc.perform(put(BASE_URI + VIDEO_ID, savedVideo.getId())
            .contentType(APPLICATION_JSON)
            .content(
                objectMapper.writeValueAsString(
                    VideoUpdateRequest.builder()
                        .title("t")
                        .description("")
                        .build()
                )));

        // then
        res.andExpect(status().isUnauthorized())
            .andDo(print());
    }

    @Test
    @DisplayName("user1가 user2의 영상을 삭제 시도한다. - 401 에러")
    void scenario5() throws Exception {
        // given
        Video savedVideo = videoRepository.save(createVideo(0L, 0L, user2));

        // when
        ResultActions res = mockMvc.perform(delete(BASE_URI + VIDEO_ID, savedVideo.getId()));

        // then
        res.andExpect(status().isUnauthorized())
            .andDo(print());
    }

    @Test
    @DisplayName("사용자가 모든 영상 목록을 조회순으로 조회한다.")
    void scenario6() throws Exception {
        // given
        List<Video> videos = List.of(
            createVideo(2L, 2L, user1),
            createVideo(1L, 2L, user1),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(0L, 2L, user2),
            createVideo(3L, 2L, user1)
            );

        videoRepository.saveAll(videos);

        // when
        ResultActions res = mockMvc.perform(
            get(BASE_URI)
                .queryParam("sortBy", "hits")
        );

        // then
        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()").value(10))
            .andExpect(jsonPath("$.data[0].viewCount").value(3L))
            .andExpect(jsonPath("$.data[1].viewCount").value(2L))
            .andExpect(jsonPath("$.data[2].viewCount").value(1L))
            .andExpect(jsonPath("$.data[3].viewCount").value(0L))
            .andDo(print());
    }

    @Test
    @DisplayName("사용자가 모든 영상 목록을 좋아요순으로 조회한다.")
    void scenario7() throws Exception {
        // given
        List<Video> videos = List.of(
            createVideo(0L, 2L, user2),
            createVideo(0L, 1L, user2),
            createVideo(0L, 1L, user2)
        );

        videoRepository.saveAll(videos);

        // when
        ResultActions res = mockMvc.perform(
            get(BASE_URI)
                .queryParam("sortBy", "likes")
        );

        // then
        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()").value(3))
            .andExpect(jsonPath("$.data[0].likeCount").value(2L))
            .andExpect(jsonPath("$.data[1].likeCount").value(1L))
            .andExpect(jsonPath("$.data[2].likeCount").value(1L))
            .andDo(print());
    }

    private ResultActions saveVideo() throws Exception {
        return mockMvc.perform(multipart(BASE_URI)
            .file(VIDEO_FILE)
            .file(THUMBNAIL_FILE)
            .param("category", "category")
            .param("title", TITLE)
            .param("description", DESCRIPTION)
        );
    }


    private Video createVideo(Long hits, Long likes, Member author) {
        return Video.builder()
            .title(TITLE)
            .description(DESCRIPTION)
            .likeCount(likes)
            .videoUrl(VIDEO_URL)
            .viewCount(hits)
            .thumbnailUrl(THUMBNAIL_URL)
            .member(author)
            .build();
    }

    private Member getUser(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
            NotExistMemberException::new);
    }
}
