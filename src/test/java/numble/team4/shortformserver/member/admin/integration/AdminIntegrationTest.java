package numble.team4.shortformserver.member.admin.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.testCommon.mockUser.WithMockCustomUser;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@BaseIntegrationTest
@AutoConfigureMockMvc(addFilters = false)
class AdminIntegrationTest {

    private static final String URI = "/v1/admin/videos";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member user2;

    @BeforeEach
    void setUp() {
        Member user1 = Member.builder()
            .name("user1")
            .role(Role.MEMBER)
            .email("user1@test.com")
            .emailVerified(true)
            .build();

        user2 = Member.builder()
            .name("user2")
            .role(Role.MEMBER)
            .email("user2@test.com")
            .emailVerified(true)
            .build();

        memberRepository.saveAll(List.of(user1, user2));

        List<Video> videoList = List.of(
            createVideo(10L, 1L, user1),
            createVideo(8L, 2L, user1),
            createVideo(8L, 2L, user1),
            createVideo(8L, 3L, user1),
            createVideo(5L, 3L, user1),
            createVideo(5L, 3L, user2),
            createVideo(5L, 5L, user2),
            createVideo(3L, 5L, user2),
            createVideo(3L, 5L, user2),
            createVideo(2L, 8L, user2),
            createVideo(2L, 8L, user2),
            createVideo(1L, 10L, user2)
        );
        videoRepository.saveAll(videoList);
    }

    private Video createVideo(long hits, Long likes,
        Member member) {
        Category category = categoryRepository.findByName("구두/로퍼")
            .orElseThrow(NotFoundCategoryException::new);
        return Video.builder()
            .title("제목")
            .description("공통")
            .member(member)
            .videoUrl("VIDEO_URL")
            .thumbnailUrl("THUMBNAIL_URL")
            .price(1000)
            .usedStatus(false)
            .likeCount(0L)
            .category(category)
            .viewCount(hits)
            .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("일반 유저가 관리자페이지 접근 - 실패")
    void memberAccessTheAdminPage() throws Exception {
        // given

        // when
        ResultActions res = mockMvc.perform(
            get(URI)
                .queryParam("page", "0")
                .queryParam("size", "3")
        );

        // then

        res.andExpect(status().isForbidden())
            .andDo(print());
    }

    @Test
    @DisplayName("관리자 페이지 영상 리스트 조회 - 성공")
    @WithMockCustomUser(role = Role.ADMIN)
    void adminPageGetVideoList() throws Exception {
        // given
        long count = videoRepository.count();

        // when
        ResultActions res = mockMvc.perform(
            get(URI)
                .queryParam("page", "1")
                .queryParam("size", "3")
                .queryParam("sort", "")
        );

        // then
        assertThat(count).isEqualTo(12);
        res.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("관리자 페이지 유저의 영상 목록")
    @WithMockCustomUser(role = Role.ADMIN)
    void adminPageGetVideoListByMember() throws Exception {
        // given
        long count = videoRepository.count();

        // when
        ResultActions res = mockMvc.perform(
            get(URI)
                .queryParam("page", "0")
                .queryParam("size", "10")
                .queryParam("user_id", String.valueOf(user2.getId()))
        );

        // then
        res.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.size()").value(7))
            .andDo(print());
    }
}