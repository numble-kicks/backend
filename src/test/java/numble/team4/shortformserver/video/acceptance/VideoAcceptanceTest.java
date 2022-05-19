package numble.team4.shortformserver.video.acceptance;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPDATE_VIDEO;
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

import java.io.UnsupportedEncodingException;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.testCommon.mockUser.WithMockCustomUser;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@WithMockCustomUser(name = "numble", email = "numble@numble.com")
class VideoAcceptanceTest extends BaseAcceptanceTest {

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

    @Autowired
    CategoryRepository categoryRepository;

    Member user1, user2;
    Category category;

    @BeforeEach
    void setUp() {
        category = categoryRepository.findByName("기타").orElseThrow(NotFoundCategoryException::new);
        user1 = getUser();
        user2 = Member.builder()
            .name("tester")
            .role(MEMBER)
            .emailVerified(false)
            .build();

        memberRepository.save(user2);
    }

    @DisplayName("user1은 영상을 등록하고 자신의 영상 정보를 조회한다.")
    @Test
    void scenario1() throws Exception {
        // when
        ResultActions res = saveVideo();

        // then
        MvcResult mvcResult = res
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(UPLOAD_VIDEO.getMessage()))
            .andDo(print())
            .andReturn();

        Long videoId = StringToJSON(mvcResult);

        // given - 영상 조회
        Video video = videoRepository.findById(videoId).orElseThrow(NotExistVideoException::new);

        // when
        ResultActions read = mockMvc.perform(get(BASE_URI + VIDEO_ID, videoId));

        // then
        read.andExpect(status().isOk())
            .andDo(print());

        assertThat(video.getViewCount()).isEqualTo(1L);

        amazonS3Uploader.deleteToS3(video.getVideoUrl());
        amazonS3Uploader.deleteToS3(video.getThumbnailUrl());
    }

    private Long StringToJSON(MvcResult mvcResult)
        throws UnsupportedEncodingException, ParseException {
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONParser parser = new JSONParser(contentAsString);
        return Long.parseLong(parser.object().get("data").toString());
    }

    @Test
    @DisplayName("user1은 저장한 영상을 수정한다.")
    void scenario2() throws Exception {
        // given
        Video video = createVideo(0L, 0L, user1);
        Video savedVideo = videoRepository.save(video);
        VideoUpdateRequest req = VideoUpdateRequest.builder()
            .title(TITLE)
            .description("설명 수정")
            .category("구두/로퍼")
            .price(100000)
            .used_status(true)
            .build();

        // when
        MvcResult res = mockMvc.perform(put(BASE_URI + VIDEO_ID, savedVideo.getId())
                .content(objectMapper.writeValueAsString(req))
                .contentType(APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(UPDATE_VIDEO.getMessage()))
            .andDo(print())
            .andReturn();

        Video updatedVideo = videoRepository.findById(StringToJSON(res))
            .orElseThrow(NotExistVideoException::new);

        // then
        assertThat(updatedVideo.getDescription()).isNotEqualTo(DESCRIPTION);
        assertThat(updatedVideo.getCategory()).isNotEqualTo(category);
    }

    @Test
    @DisplayName("user1은 영상 두 개를 저장하고, 마지막에 등록한 영상을 삭제한다.")
    void scenario3() throws Exception {
        // given
        saveVideo();
        saveVideo();

        Video video = videoRepository.findAll().get(1);

        // when
        ResultActions res = mockMvc.perform(delete(BASE_URI + VIDEO_ID, video.getId()));

        // then
        res.andExpect(status().isOk())
            .andDo(print());
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
                        .category("기타")
                        .price(100000)
                        .used_status(false)
                        .build()
                )));

        // then
        res.andExpect(status().isForbidden())
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
        res.andExpect(status().isForbidden())
            .andDo(print());
    }

    private ResultActions saveVideo() throws Exception {
        return mockMvc.perform(multipart(BASE_URI)
            .file(VIDEO_FILE)
            .file(THUMBNAIL_FILE)
            .param("price", "1000")
            .param("used_status", "false")
            .param("category", "기타")
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
            .usedStatus(false)
            .price(10000)
            .category(category)
            .viewCount(hits)
            .thumbnailUrl(THUMBNAIL_URL)
            .member(author)
            .build();
    }

    private Member getUser() {
        return memberRepository.findByEmail("numble@numble.com").orElseThrow(
            NotExistMemberException::new);
    }
}
