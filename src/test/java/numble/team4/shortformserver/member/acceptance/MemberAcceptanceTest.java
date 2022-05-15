package numble.team4.shortformserver.member.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.ui.dto.MemberEmailRequest;
import numble.team4.shortformserver.member.member.ui.dto.MemberNameUpdateRequest;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.testCommon.mockUser.WithMockCustomUser;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_MEMBER;
import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private EntityManager entityManager;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder().name("user2").role(Role.MEMBER).build();
        entityManager.persist(member);
        createVideo();
    }
    void createVideo() {
        for (int i = 0; i < 20; i++) {
            Video video = Video.builder()
                    .member(member)
                    .videoUrl("http://videourl.com")
                    .thumbnailUrl("http://url.com")
                    .title("title")
                    .price(5000)
                    .usedStatus(true)
                    .description("description")
                    .build();
            entityManager.persist(video);
        }
    }
    void createLikeVideo() {
        List<Video> all = videoRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, all.get(i));
            entityManager.persist(likeVideo);
        }
    }

    static Stream<Long> valueSources() {
        return Stream.of(null, 3L, 8L, 30L);
    }


    @ParameterizedTest
    @DisplayName("[성공] 1. 존재하는 사용자가 업로드한 영상 목록 조회")
    @MethodSource("valueSources")
    void findAllVideosByMember_isok_success(Long id) throws Exception {
        //given
        long count = 18;
        if (id != null) {
            count = videoRepository.findAll().stream().map(x -> x.getId())
                    .filter(x -> x < id)
                    .count();
        }

        //when
        ResultActions res = mockMvc.perform(
                get("/v1/users/{memberId}/videos", member.getId())
                        .param("last_id", (id == null) ? null : String.format("%s", id))
        );


        //then
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value((count > 18) ? 18 : count))
                .andExpect(jsonPath("$.message").value(GET_ALL_VIDEOS_OF_MEMBER.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("[실패] 1. 존재하지 않는 사용자가 업로드한 영상 목록 조회")
    void findAllVideosByMember_notExistMemberException_fail() throws Exception {
        //when
        ResultActions res = mockMvc.perform(
                get("/v1/users/{memberId}/videos", 88979L)
                        .param("last_id", "1")
        );

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                .andDo(print());
    }

    @ParameterizedTest
    @DisplayName("[성공] 1. 존재하는 사용자가 좋아요를 누른 영상 목록 조회")
    @MethodSource("valueSources")
    void findAllLikeVideosByMember_isok_success(Long id) throws Exception {
        //given
        createLikeVideo();
        long count = 18;
        if (id != null) {
            count = videoRepository.findAll().stream().map(x -> x.getId())
                    .filter(x -> x < id)
                    .count();
        }

        //when
        ResultActions res = mockMvc.perform(
                get("/v1/users/{memberId}/likes", member.getId())
                        .param("last_id", (id == null) ? null : String.format("%s", id))
        );


        //then
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value((count > 18) ? 18 : count))
                .andExpect(jsonPath("$.message").value(GET_ALL_LIKE_VIDEOS_OF_MEMBER.getMessage()))
                .andDo(print());
    }

    @Test
    @DisplayName("[실패] 1. 존재하지 않는 사용자가 좋아요를 누른 영상 목록 조회")
    void findAllLikeVideosByMember_notExistMemberException_fail() throws Exception {
        //when
        ResultActions res = mockMvc.perform(
                get("/v1/users/{memberId}/likes", 98798798L)
                        .param("last_id", "1")
        );

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                .andDo(print());
    }

    @Nested
    @DisplayName("사용자의 정보 조회")
    class GetUserInfoTest {
        
        @Test
        @DisplayName("[성공] 존재하는 사용자의 정보 조회")
        void getUserInfo_isok_success() throws Exception {
            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/users/{memberId}", member.getId())
            );
            
            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(GET_MEMBER_INFO.getMessage()))
                    .andExpect(jsonPath("$.data.id").value(member.getId()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 사용자의 정보 조회")
        void getUserInfo_notExistMemberException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/users/{memberId}", 3048902834L)
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                    .andDo(print());
        }
    }

    @Test
    @WithMockCustomUser
    @DisplayName("[성공]1. 사용자의 닉네임 수정")
    void updateUserName_isok_success() throws Exception {
        //given
        MemberNameUpdateRequest request = new MemberNameUpdateRequest("kebin");
        //when
        ResultActions res = mockMvc.perform(
                put("/v1/users/name")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
        );

        //then
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(UPDATE_USER_NAME.getMessage()))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("[성공] 1. 사용자의 이메일 등록")
    void saveEmail_isok_success() throws Exception {
        //given
        MemberEmailRequest request = new MemberEmailRequest("test@numble.com");

        //when
         ResultActions res = mockMvc.perform(
                        post("/v1/users/email")
                                .contentType(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                );

        //then
        res.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(SAVE_MEMBER_EMAIL.getMessage()));
    }
}
