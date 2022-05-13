package numble.team4.shortformserver.member.integration;

import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.ui.MemberController;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BaseIntegrationTest
public class MemberIntegrationTest {

    @Autowired
    MemberController memberController;

    @Autowired
    EntityManager entityManager;

    @Autowired
    VideoRepository videoRepository;

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
    @DisplayName("[성공] 마이비디오 목록 조회 (videoId가 null일 때)")
    @MethodSource("valueSources")
    void findAllVideosByMember_returnListHasSize18_success(Long value) {
        //given
        createLikeVideo();
        long count = 18;
        if (value != null) {
            count = videoRepository.findAll().stream()
                    .map(x -> x.getId())
                    .filter(x -> x < value)
                    .count();
        }

        //when
        List<VideoListResponse> res = memberController.findAllVideosByMember(member.getId(), value).getData();

        //then
        assertThat(res).hasSize((count > 18) ? 18 : (int) count);
        for (int i = 0; i < res.size() - 1; i++) {
            assertTrue(res.get(i).getId() > res.get(i + 1).getId());
        }
    }

    @ParameterizedTest
    @DisplayName("[성공] 존재하는 사용자의 좋아요 동영상 목록 조회")
    @MethodSource("valueSources")
    void findAllLikeVideosByMember_returnListHasSizeLessThanEqual15_success(Long value) {
        //given
        createLikeVideo();
        long count = 18;
        if (value != null) {
            count = videoRepository.findAll().stream()
                    .map(x -> x.getId())
                    .filter(x -> x < value)
                    .count();
        }

        //when
        List<VideoListResponse> res = memberController.findAllLikeVideosByMember(member.getId(), value).getData();

        //then
        assertThat(res).hasSize((count > 18) ? 18 : (int) count);
        for (int i = 0; i < res.size() - 1; i++) {
            assertTrue(res.get(i).getId() > res.get(i + 1).getId());
        }

    }

}
