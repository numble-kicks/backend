package numble.team4.shortformserver.video.integration;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import numble.team4.shortformserver.video.ui.VideoSearchController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@BaseIntegrationTest
class VideoSearchIntegrationTest {

    @Autowired
    private VideoSearchController videoSearchController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member;
    private Category category;
    private List<Video> videos;
    private List<Long> ids;

    @BeforeEach
    void setUp() {
        category = categoryRepository.findByName("기타")
            .orElseThrow(NotFoundCategoryException::new);

        member = Member
            .builder()
            .name("user1")
            .email("user@usser.com")
            .emailVerified(true)
            .role(MEMBER)
            .build();
        memberRepository.save(member);

        videos = List.of(
            createVideo(10L, 1L, "우르오스", "공통"),
            createVideo(8L, 2L, "나이키", "공통"),
            createVideo(8L, 2L, "범고래", "공통 나이키"),
            createVideo(8L, 3L, "뉴발 992", "공통"),
            createVideo(5L, 3L, "에어맥스", "공통 나이키"),
            createVideo(5L, 3L, "아이폰", "공통"),
            createVideo(5L, 5L, "알파바운스", "공통"),
            createVideo(3L, 5L, "로퍼", "공통"),
            createVideo(3L, 5L, "발렌시아가", "공통"),
            createVideo(2L, 8L, "스피드러너", "공통 발렌시아가"),
            createVideo(2L, 8L, "맥퀸", "공통"),
            createVideo(1L, 10L, "골든구스", "공통")
        );
        videoRepository.saveAll(videos);
        Video video = videos.get(0);
        ids = List.of(
            video.getId(),
            video.getId() + 1,
            video.getId() + 2,
            video.getId() + 3,
            video.getId() + 4,
            video.getId() + 5,
            video.getId() + 6,
            video.getId() + 7,
            video.getId() + 8,
            video.getId() + 9,
            video.getId() + 10,
            video.getId() + 11
        );
    }

    private Video createVideo(long viewCount, long likeCount, String title, String description) {
        return Video.builder()
            .title(title)
            .description(description)
            .price(10000)
            .category(category)
            .usedStatus(false)
            .videoUrl("video URL")
            .thumbnailUrl("thumbnail URL")
            .member(member)
            .viewCount(viewCount)
            .likeCount(likeCount)
            .build();
    }

    @Test
    @DisplayName("검색 기능 - 성공")
    void findByKeyword() {
        // given
        String keyword = "공통";
        Object[] values = ids.stream().sorted(Collections.reverseOrder()).toArray();

        // when
        List<VideoListResponse> res = videoSearchController.searchVideoByKeyword(keyword, null, null)
            .getData();

        // then
        assertThat(res)
            .hasSize(videos.size())
            .extracting("id")
            .containsExactly(values);
    }

    @Test
    @DisplayName("검색 기능 조회순 정렬 - 성공")
    void findByKeyword_sortByHits() {
        // given
        String keyword = "공통";
        Object[] values = new Object[]{
            ids.get(0),
            ids.get(3),
            ids.get(2),
            ids.get(1),
            ids.get(6),
            ids.get(5),
            ids.get(4),
            ids.get(8),
            ids.get(7),
            ids.get(10),
            ids.get(9),
            ids.get(11)
        };

        // when
        List<VideoListResponse> lastId가_null = videoSearchController.searchVideoByKeyword(keyword, null,
            "hits").getData();

        // then
        assertThat(lastId가_null)
            .hasSize(videos.size())
            .extracting("id")
            .containsExactly(values);
    }

    @Test
    @DisplayName("검색 기능 조회순 정렬 - 성공, 커서가 8일 경우")
    void findByKeyword_sortByHits_cursor() {
        // given
        String keyword = "공통";
        Object[] values = new Object[]{
            ids.get(10),
            ids.get(9),
            ids.get(11)
        };

        // when
        List<VideoListResponse> lastId는8 = videoSearchController.searchVideoByKeyword(keyword, ids.get(7),
            "hits").getData();

        // then
        assertThat(lastId는8)
            .hasSize(values.length)
            .extracting("id")
            .containsExactly(values);
    }

    @Test
    @DisplayName("검색 기능 조회순 정렬 - 성공, 나이키로 검색 했을경우")
    void findByKeyword_cursor_id_exist() {
        // given
        String keyword = "나이키";
        Object[] value = new Object[] {3L, 2L, 5L};

        // when
        List<VideoListResponse> 정렬_순서_325 = videoSearchController.searchVideoByKeyword(keyword, ids.get(0),
            "hits").getData();

        // then
        assertThat(정렬_순서_325)
            .hasSize(value.length)
            .extracting("id")
            .containsExactly(value);
    }

    @Test
    @DisplayName("검색 페이지 top10 영상 가져오기 - 성공")
    void getVideoTop10() {
        // given
        Object[] hitsRes = new Object[]{
            videos.get(0).getId(),
            videos.get(3).getId(),
            videos.get(2).getId(),
            videos.get(1).getId(),
            videos.get(6).getId(),
            videos.get(5).getId(),
            videos.get(4).getId(),
            videos.get(8).getId(),
            videos.get(7).getId(),
            videos.get(10).getId(),
        };

        Object[] likesRes = new Object[]{
            videos.get(11).getId(),
            videos.get(10).getId(),
            videos.get(9).getId(),
            videos.get(8).getId(),
            videos.get(7).getId(),
            videos.get(6).getId(),
            videos.get(5).getId(),
            videos.get(4).getId(),
            videos.get(3).getId(),
            videos.get(2).getId(),
        };

        // when
        List<VideoListResponse> hits = videoSearchController.getVideoTop10("hits").getData();
        List<VideoListResponse> likes = videoSearchController.getVideoTop10("likes").getData();

        // then
        assertThat(hits)
            .extracting("id")
            .containsExactly(hitsRes);

        assertThat(likes)
            .extracting("id")
            .containsExactly(likesRes);
    }

}
