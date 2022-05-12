package numble.team4.shortformserver.member.acceptance;

import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_MEMBER;
import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.GET_ALL_LIKE_VIDEOS_OF_MEMBER;
import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.GET_ALL_VIDEOS_OF_MEMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                        .param("last_video_id", (id == null) ? null : String.format("%s", id))
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
                        .param("last_video_id", "1")
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
                        .param("last_video_id", (id == null) ? null : String.format("%s", id))
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
                        .param("last_video_id", "1")
        );

        //then
        res.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                .andDo(print());
    }


}
