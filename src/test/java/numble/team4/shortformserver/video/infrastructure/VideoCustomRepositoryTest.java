package numble.team4.shortformserver.video.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;
import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.category.domain.Category;
import numble.team4.shortformserver.video.category.domain.CategoryRepository;
import numble.team4.shortformserver.video.category.exception.NotFoundCategoryException;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@BaseDataJpaTest
class VideoCustomRepositoryTest {

    private static final int PAGE = 18;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member1, member2;
    private List<Video> videos;

    static Stream<Long> valueSources() {
        return Stream.of(null, 3L, 8L, 30L);
    }

    @BeforeEach
    void setUp() {
        member2 = Member.builder()
            .name("member2")
            .role(Role.MEMBER)
            .emailVerified(true)
            .email("tester1@email.com")
            .build();
        member1 = Member.builder()
            .name("tester")
            .role(Role.MEMBER)
            .emailVerified(true)
            .email("tester@email.com")
            .build();
        memberRepository.saveAll(List.of(member1, member2));

        videos = List.of(
            createVideo(1L, 1L, "우르오스", "우르오스", member1),
            createVideo(3L, 1L, "나이키", "나이키 신발", member1),
            createVideo(3L, 1L, "범고래", "나이키 덩크로우 범고래, 드로우 신발 상품", member1),
            createVideo(5L, 1L, "뉴발 992", "뉴발란스 992 신발", member1),
            createVideo(5L, 1L, "에어맥스", "나이키 에어맥스95 신발", member2),
            createVideo(10L, 1L, "아이폰", "애플", member2)
        );
        videoRepository.saveAll(videos);
    }

    private Video createVideo(long hits, Long likes, String title, String des,
        Member member) {
        Category category = categoryRepository.findByName("구두/로퍼")
            .orElseThrow(NotFoundCategoryException::new);
        return Video.builder()
            .title(title)
            .description(des)
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

    void createLikeVideo() {
        List<Video> all = videoRepository.findAll();
        for (Video video : all) {
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member1, video);
            testEntityManager.persist(likeVideo);
        }
    }

    @ParameterizedTest
    @DisplayName("[성공] 사용자의 동영상 목록을 조회 (videoId가 null일 때)")
    @MethodSource("valueSources")
    void findAllByMemberAndMaxVideoId_returnListHasSizeLessThanAndEqualLimitNum_success(Long id) {
        //given
        long count = 5;
        if (id != null) {
            count = videoRepository.findAll().stream().map(Video::getId)
                .filter(x -> x < id)
                .count();
        }

        //when
        List<Video> res = videoRepository.findAllByMemberAndMaxVideoId(member1, id, 5);

        //then
        assertThat(res).hasSize((count > 5) ? 5 : (int) count);

        for (int i = 0; i < res.size() - 1; i++) {
            assertTrue(res.get(i).getId() > res.get(i + 1).getId());
        }
    }

    @ParameterizedTest
    @DisplayName("[성공] 사용자가 좋아요한 동영상 목록을 조회")
    @MethodSource("valueSources")
    void findAllLikeVideoByMemberAndMaxVideoId_returnListHasSizeLessThanAndEqualLimitNum_success(
        Long id) {
        //given
        createLikeVideo();
        long count = 5;
        if (id != null) {
            count = videoRepository.findAll().stream().map(Video::getId)
                .filter(x -> x < id)
                .count();
        }

        //when
        List<Video> res = videoRepository.findAllLikeVideoByMemberAndMaxVideoId(member1, id, 5);

        //then
        assertThat(res).hasSize((count > 5) ? 5 : (int) count);

        for (int i = 0; i < res.size() - 1; i++) {
            assertTrue(res.get(i).getId() > res.get(i + 1).getId());
        }
    }

    @Test
    @DisplayName("검색 조회 - 성공, 최신순 정렬(default)")
    void findByKeyword() {
        // given
        Long firstId = videos.get(0).getId();

        List<Long> ids = List.of(
            firstId,
            firstId + 1, // 나이키
            firstId + 2,
            firstId + 3, // 나이키
            firstId + 4, // 나이키
            firstId + 5 // 애플
        );

        // when
        List<Video> 키워드나이키최신순_id는_421순 = videoRepository.searchVideoByKeyword(null, "나이키", null);
        List<Video> 키워드아이폰_id는_5 = videoRepository.searchVideoByKeyword(null, "아이폰", null);
        List<Video> 키워드신발최신순_id_321순_커서적용 = videoRepository.searchVideoByKeyword(ids.get(4), "신발",
            "");

        // then
        assertThat(키워드아이폰_id는_5).hasSize(1);
        assertThat(키워드나이키최신순_id는_421순)
            .hasSize(3)
            .extracting("id")
            .containsExactly(
                ids.get(4),
                ids.get(2),
                ids.get(1)
            );

        assertThat(키워드신발최신순_id_321순_커서적용)
            .hasSize(3)
            .extracting("id")
            .containsExactly(
                ids.get(3),
                ids.get(2),
                ids.get(1)
            );
    }

    @Test
    @DisplayName("검색 조회 - 성공, 커서 기반 조회순 정렬 ")
    void findByKeyword_sortBy() {
        // given
        Long firstId = videos.get(0).getId();

        List<Long> ids = List.of(
            firstId,
            firstId + 1, // 신발
            firstId + 2, // 신발
            firstId + 3, // 신발
            firstId + 4, // 신발
            firstId + 5
        );
        // when
        List<Video> 키워드신발조회순_id는_4321순 = videoRepository.searchVideoByKeyword(null, "신발", "hits");
        List<Video> 키워드신발조회순_id는_321순_커서적용 = videoRepository.searchVideoByKeyword(ids.get(4), "신발",
            "hits");

        // then
        assertThat(키워드신발조회순_id는_4321순)
            .hasSize(4)
            .extracting("id")
            .containsExactly(
                ids.get(4),
                ids.get(3),
                ids.get(2),
                ids.get(1)
            );

        assertThat(키워드신발조회순_id는_321순_커서적용)
            .hasSize(3)
            .extracting("id")
            .containsExactly(
                ids.get(3),
                ids.get(2),
                ids.get(1)
            );
    }

    @Test
    @DisplayName("검색 조회 - 성공, 포함된 검색어가 없는 경우")
    void findByKeyword_notIncludeKeyword() {
        // when
        List<Video> 해당하는_검색어가_없을때_사이즈는_0 = videoRepository.searchVideoByKeyword(null, "ㅁㄴㅇㄹ", null);

        // then
        assertThat(해당하는_검색어가_없을때_사이즈는_0).isEmpty();
    }

    @Test
    @DisplayName("검색 페이지 - 좋아요 Best 10개, 조회 Best 10개")
    void searchPage() {
        // given
        videoRepository.deleteAll(videos);
        List<Video> videoList = List.of(
            createVideo(10L, 1L, "제목", "공통", member1),
            createVideo(8L, 2L, "제목", "공통", member1),
            createVideo(8L, 2L, "제목", "공통", member1),
            createVideo(8L, 3L, "제목", "공통", member1),
            createVideo(5L, 3L, "제목", "공통", member1),
            createVideo(5L, 3L, "제목", "공통", member1),
            createVideo(5L, 5L, "제목", "공통", member1),
            createVideo(3L, 5L, "제목", "공통", member1),
            createVideo(3L, 5L, "제목", "공통", member1),
            createVideo(2L, 8L, "제목", "공통", member1),
            createVideo(2L, 8L, "제목", "공통", member1),
            createVideo(1L, 10L, "제목", "공통", member1)
        );
        videoRepository.saveAll(videoList);

        Object[] hitsRes = new Object[]{
            videoList.get(0).getId(),
            videoList.get(3).getId(),
            videoList.get(2).getId(),
            videoList.get(1).getId(),
            videoList.get(6).getId(),
            videoList.get(5).getId(),
            videoList.get(4).getId(),
            videoList.get(8).getId(),
            videoList.get(7).getId(),
            videoList.get(10).getId(),
        };

        Object[] likesRes = new Object[]{
            videoList.get(11).getId(),
            videoList.get(10).getId(),
            videoList.get(9).getId(),
            videoList.get(8).getId(),
            videoList.get(7).getId(),
            videoList.get(6).getId(),
            videoList.get(5).getId(),
            videoList.get(4).getId(),
            videoList.get(3).getId(),
            videoList.get(2).getId(),
        };

        // when
        List<Video> hits = videoRepository.getTopVideos("hits", 10);
        List<Video> likes = videoRepository.getTopVideos("likes", 10);

        // then
        assertThat(hits)
            .extracting("id")
            .containsExactly(hitsRes);

        assertThat(likes)
            .extracting("id")
            .containsExactly(likesRes);
    }

    @Test
    @DisplayName("admin 영상 리스트 페이징 테스트")
    void admin_video_() {
        // given
        long total = videoRepository.count();

        // when
        Page<Video> page0size3 = videoRepository.getAllVideos(PageRequest.of(0, 3), total, null);
        Page<Video> page1size3 = videoRepository.getAllVideos(PageRequest.of(1, 3), total, null);

        // then
        assertThat(page0size3.getContent().get(0).getTitle()).isEqualTo("우르오스");
        assertThat(page1size3.getContent().get(0).getTitle()).isEqualTo("뉴발 992");
    }


    @Test
    @DisplayName("admin 영상 리스트 사용자별 영상 목록 조회 테스트")
    void adminVideoByMember() {
        // given
        long total = videoRepository.count();

        // when
        Page<Video> member1Videos = videoRepository.getAllVideos(PageRequest.of(0, 10), total,
            member1.getId());
        Page<Video> member2Videos = videoRepository.getAllVideos(PageRequest.of(0, 10), total,
            member2.getId());

        // then
        assertThat(member1Videos.getContent()).hasSize(4);
        assertThat(member2Videos.getContent()).hasSize(2);
    }
}