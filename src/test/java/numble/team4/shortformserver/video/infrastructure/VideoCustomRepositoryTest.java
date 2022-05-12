package numble.team4.shortformserver.video.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@BaseDataJpaTest
class VideoCustomRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member;
    private List<Video> videos;

    @BeforeEach
    void setUp() {
        member = Member.builder()
            .name("tester")
            .role(Role.MEMBER)
            .emailVerified(true)
            .email("tester@email.com")
            .build();
        memberRepository.save(member);

        videos = List.of(
            createVideo(1L, 1L, 1),
            createVideo(3L, 1L, 2),
            createVideo(3L, 1L, 3),
            createVideo(5L, 10L, 4),
            createVideo(5L, 10L, 5),
            createVideo(5L, 110L, 6)
        );

        for (Video v : videoRepository.saveAll(videos)) {
            v.updateCursor();
        }
    }

    private Video createVideo(long likes, long hits, long id) {
        Category category = categoryRepository.findByName("구두/로퍼")
            .orElseThrow(NotFoundCategoryException::new);
        return Video.builder()
            .title("Title")
            .description("description")
            .member(member)
            .videoUrl("VIDEO_URL")
            .thumbnailUrl("THUMBNAIL_URL")
            .price(1000)
            .usedStatus(false)
            .likeCount(likes)
            .category(category)
            .viewCount(hits)
            .build();
    }

    private String getCursor(Long sort, Long id) {
        return String.format("%05d", sort) + String.format("%05d", id);
    }

    @Test
    @DisplayName("좋아요 순 정렬")
    void sortByLikes() throws Exception {
        // given
        List<VideoSort> sorts = videos.stream().map(VideoSort::from).sorted()
            .collect(Collectors.toList());

        // when
        List<Video> v = videoRepository.findAllVideos(null, "likes", PageRequest.of(0, 5))
            .getContent();

        // then
        assertThat(v)
            .extracting("id")
            .containsExactly(
                sorts.get(0).id,
                sorts.get(1).id,
                sorts.get(2).id,
                sorts.get(3).id,
                sorts.get(4).id
            );
    }

    @Test
    @DisplayName("좋아요 순 정렬 - 커서 기반")
    void sortByLikes_cursorBased() throws Exception {
        // given
        List<VideoSort> sorts = videos.stream().map(VideoSort::from).sorted()
            .collect(Collectors.toList());
        // when
        List<Video> v = videoRepository.findAllVideos(sorts.get(0).cursor, "likes",
                PageRequest.of(0, 5))
            .getContent();

        // then
        assertThat(v)
            .extracting("id")
            .containsExactly(
                sorts.get(1).id,
                sorts.get(2).id,
                sorts.get(3).id,
                sorts.get(4).id,
                sorts.get(5).id

            );
    }

    static class VideoSort implements Comparable<VideoSort> {

        private Long id;
        private Long likes;
        private String cursor;

        public VideoSort(Long id, Long sort, String cursor) {
            this.id = id;
            this.likes = sort;
            this.cursor = cursor;
        }

        private static VideoSort from(Video v) {
            return new VideoSort(v.getId(), v.getLikeCount(), v.getLikesCursor());
        }

        @Override
        public int compareTo(VideoSort v) {
            if (Objects.equals(this.likes, v.likes)) {
                return (int) (v.id - this.id);
            }
            return (int) (v.likes - this.likes);
        }
    }

}