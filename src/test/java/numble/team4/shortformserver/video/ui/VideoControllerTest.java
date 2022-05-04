package numble.team4.shortformserver.video.ui;

import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_AUTHOR_EXCEPTION;
import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_VIDEO;
import static numble.team4.shortformserver.video.ui.VideoResponseMessage.UPDATE_VIDEO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.video.application.VideoService;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = VideoController.class)
@MockBean(JpaMetamodelMappingContext.class)
class VideoControllerTest {

    private static final String VIDEO_URI = "/v1/videos";
    private static final String VIDEO_ID = "/{video_id}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private VideoService videoService;

    private VideoRequest videoRequest;
    private Member member;
    private Video video;
    private MockMultipartFile videos;
    private MockMultipartFile thumbnail;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .id(10L)
            .build();

        video = Video.builder()
            .id(10L)
            .title("title")
            .description("description")
            .videoUrl("test url")
            .thumbnailUrl("test url")
            .member(member)
            .viewCount(0L)
            .likeCount(0L)
            .build();
    }

    @Nested
    @DisplayName("Video 수정 테스트")
    class updateVideo {
        @Test
        @DisplayName("video 수정 - 성공")
        void updateVideo_success() throws Exception {
            // given
            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
                .title("update title")
                .description("description")
                .build();

            VideoResponse videoResponse = VideoResponse.of(video);

            // when
            when(
                videoService.updateVideo(any(VideoUpdateRequest.class),anyLong(), anyLong())).thenReturn(
                videoResponse);

            ResultActions res = mockMvc.perform(
                MockMvcRequestBuilders.put(VIDEO_URI + VIDEO_ID, video.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(videoUpdateRequest))
                    .queryParam("memberId", String.valueOf(member.getId()))
            );

            // then
            res.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(video.getId()))
                .andExpect(jsonPath("$.message").value(UPDATE_VIDEO.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("Video 수정 - 실패, 존재하지 않는 영상일 경우")
        void updateVideo_notExistVideo() throws Exception {
            // given
            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
                .title("update title")
                .description("description")
                .build();

            // when
            doThrow(new NotExistVideoException()).when(videoService)
                .updateVideo(any(VideoUpdateRequest.class), anyLong(), anyLong());

            ResultActions res = mockMvc.perform(
                MockMvcRequestBuilders.put(VIDEO_URI + VIDEO_ID, video.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(videoUpdateRequest))
                    .queryParam("memberId", String.valueOf(member.getId()))
            );

            // then
            res.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_VIDEO.getMessage()))
                .andDo(print());
        }

        @Test
        @DisplayName("Video 수정 - 실패, 접근 권한이 없는 경우")
        void updateVideo_notAuthor() throws Exception {
            // given
            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
                .title("update title")
                .description("description")
                .build();

            // when
            doThrow(new NotAuthorException()).when(videoService)
                .updateVideo(any(VideoUpdateRequest.class), anyLong(), anyLong());

            ResultActions res = mockMvc.perform(
                MockMvcRequestBuilders.put(VIDEO_URI + VIDEO_ID, video.getId())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(videoUpdateRequest))
                    .queryParam("memberId", String.valueOf(member.getId()))
            );

            // then
            res.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(NOT_AUTHOR_EXCEPTION.getMessage()))
                .andDo(print());
        }
    }
}