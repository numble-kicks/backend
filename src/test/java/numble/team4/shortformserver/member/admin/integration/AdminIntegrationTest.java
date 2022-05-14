package numble.team4.shortformserver.member.admin.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    private static final String URI = "/v1/videos/admin";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    private Member user;

    @BeforeEach
    void setUp() {
        user = Member.builder()
            .name("user")
            .role(Role.MEMBER)
            .email("user@test.com")
            .emailVerified(true)
            .build();

        memberRepository.save(user);


        List<Video> videoList = List.of(
            createVideo(10L, 1L),
            createVideo(8L, 2L),
            createVideo(8L, 2L),
            createVideo(8L, 3L),
            createVideo(5L, 3L),
            createVideo(5L, 3L),
            createVideo(5L, 5L),
            createVideo(3L, 5L),
            createVideo(3L, 5L),
            createVideo(2L, 8L),
            createVideo(2L, 8L),
            createVideo(1L, 10L)
        );
        videoRepository.saveAll(videoList);
    }
    
    private Video createVideo(long hits, Long likes) {
        Category category = categoryRepository.findByName("구두/로퍼")
            .orElseThrow(NotFoundCategoryException::new);
        return Video.builder()
            .title("제목")
            .description("공통")
            .member(user)
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
        );

        // then
        assertThat(count).isEqualTo(12);
        res.andExpect(status().isOk())
            .andDo(print());
    }
}