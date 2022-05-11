package numble.team4.shortformserver.video.infrastructure;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BaseDataJpaTest
class VideoCustomRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder().name("from").role(Role.MEMBER).build();
        testEntityManager.persist(member);
        createVideo();
    }

    void createVideo() {
        for (int i = 0; i < 8; i++) {
            Video build = Video.builder()
                    .member(member)
                    .videoUrl("videoUrl")
                    .thumbnailUrl("thumnailUrl")
                    .description("테스트 비디오")
                    .title("테스트 비디오")
                    .build();
            videoRepository.save(build);
        }
    }

    @Test
    @DisplayName("[성공] 사용자의 동영상 목록을 조회 (videoId가 null일 때)")
    void findAllByMemberAndMaxVideoId_returnListHasSizeLimitNum_success() {

        //when
        List<VideoListResponse> videos = videoRepository.findAllByMemberAndMaxVideoId(member, null, 5);

        assertThat(videoRepository.count()).isEqualTo(8);
        //then
        assertThat(videos).hasSize(5);
        for (int i = 0; i < 4; i++) {
            assertTrue(videos.get(i).getId() > videos.get(i + 1).getId());
        }
    }

    @Test
    @DisplayName("[성공] 사용자의 동영상 목록을 조회 (videoId가 null이 아닐 때)")
    void findAllByMemberAndMaxVideoId_returnListHasLeftNum_success() {
        List<Video> all = videoRepository.findAll()
                .stream()
                .sorted((a, b) -> (a.getId() > b.getId()) ? 1 : -1)
                .collect(Collectors.toList());

        Long id = all.get(3).getId();

        List<VideoListResponse> videos = videoRepository.findAllByMemberAndMaxVideoId(member, id, 5);

        assertThat(videos).hasSize(3);
        for (int i = 0; i < videos.size() - 1; i++) {
            assertTrue(videos.get(i).getId() > videos.get(i + 1).getId());
        }
    }
}