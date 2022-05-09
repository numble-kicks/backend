package numble.team4.shortformserver.likevideo.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.domain.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@BaseDataJpaTest
class LikeVideoRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LikeVideoRepository likeVideoRepository;

    private Member member;
    private Video video;

    @BeforeEach
    void init() {
        member = Member.builder()
                .role(Role.MEMBER)
                .emailVerified(true)
                .build();
        entityManager.persist(member);

        video = Video.builder()
                .videoUrl("http://videourl.com")
                .thumbnailUrl("http://url.com")
                .title("title")
                .description("description")
                .build();
        entityManager.persist(video);
    }

    @Test
    @DisplayName("[성공] likeVideo 추가 테스트")
    void save_LikeVideoListSizeOne_success() {
        //when
        LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, video);
        likeVideoRepository.save(likeVideo);

        //then
        List<LikeVideo> list = likeVideoRepository.findAll();
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo(likeVideo);

    }


    @Test
    @DisplayName("[성공] likeVideo 삭제 테스트")
    void delete_LikeVideoListSizeZero_success() {

    }
}