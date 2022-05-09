package numble.team4.shortformserver.likevideo.acceptance;


import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.testCommon.WithMockCustomUser;
import numble.team4.shortformserver.video.domain.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static numble.team4.shortformserver.common.exception.ExceptionType.ALREADY_EXIST_LIKE_VIDEO;
import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_VIDEO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeVideoAcceptanceTest extends BaseAcceptanceTest {

    @Autowired
    private EntityManager entityManager;

    private Video video;
    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder()
                .role(Role.MEMBER)
                .emailVerified(true)
                .build();
        entityManager.persist(member);

        video = Video.builder()
                .member(member)
                .videoUrl("http://videourl.com")
                .thumbnailUrl("http://url.com")
                .title("title")
                .description("description")
                .build();
        entityManager.persist(video);
    }

    @Nested
    @Rollback(false)
    @WithMockCustomUser
    @DisplayName("동영상 좋아요 등록 테스트")
    class SaveLikeVideoTest {

        @Test
        @DisplayName("[성공] 좋아요를 누르지 않은 동영상에 좋아요 등록 요청")
        void saveLikeVideo_isok_success() throws Exception {
            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/videos/{videoId}/likes", video.getId())
            );

            //then
            res.andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 동영상에 좋아요 등록 요창")
        void saveLikeVideo_notExistLikeVideoException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/videos/{videoId}/likes", 39842109L)
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_VIDEO.getMessage()));
        }

        @Test
        @DisplayName("[실패] 이미 좋아요를 누른 동영상에 좋아요 등록 요청")
        void saveLikeVideo_alreadyExistLikeVideoExceiption_fail() throws Exception {
            //given
            mockMvc.perform(
                    get("/v1/videos/{videoId}/likes", video.getId())
            );

            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/videos/{videoId}/likes", video.getId())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ALREADY_EXIST_LIKE_VIDEO.getMessage()));
        }
    }
}
