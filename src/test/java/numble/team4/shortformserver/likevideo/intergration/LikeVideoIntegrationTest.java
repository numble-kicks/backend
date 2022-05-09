package numble.team4.shortformserver.likevideo.intergration;

import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.likevideo.domain.LikeVideoRepository;
import numble.team4.shortformserver.likevideo.exception.AlreadyExistLikeVideoException;
import numble.team4.shortformserver.likevideo.exception.NotExistLikeVideoException;
import numble.team4.shortformserver.likevideo.exception.NotMemberOfLikeVideoException;
import numble.team4.shortformserver.likevideo.ui.LikeVideoController;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@BaseIntegrationTest
public class LikeVideoIntegrationTest {

    @Autowired
    private LikeVideoController likeVideoController;

    @Autowired
    private LikeVideoRepository likeVideoRepository;

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
    @DisplayName("동영상 좋아요 등록 테스트")
    class SaveLikeVideoTest {

        @Test
        @DisplayName("[성공] 좋아요 목록에 없는 동영상에 좋아요 등록 요청")
        void saveVideo_likeVideoFindAllHasOne_success() {
            //when
            likeVideoController.saveLikeVideo(member, video.getId());

            //then
            assertThat(likeVideoRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 동영상에 좋아요 등록 요청")
        void saveVideo_notExistVideoException_fail() {
            //when, then
            assertThrows(
                    NotExistVideoException.class,
                    () -> likeVideoController.saveLikeVideo(member, 33333L)
            );
        }

        @Test
        @DisplayName("[실패] 이미 좋아요를 누른 동영상에 좋아요 등록 요청")
        void saveVideo_alreadyExistLikeVideoException_fail() {
            //when
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, video);
            likeVideoRepository.save(likeVideo);

            //when,then
            assertThrows(
                    AlreadyExistLikeVideoException.class,
                    () -> likeVideoController.saveLikeVideo(member, video.getId())
            );
        }
    }

    @Nested
    @DisplayName("동영상 좋아요 삭제 테스트")
    class DeleteLikeVideoTest {

        @Test
        @DisplayName("[성공] 본인이 생성한 좋아요 삭제 요청")
        void deleteLikeVideo_likeVideoFindAllHasZero_success() {
            //given
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, video);
            likeVideoRepository.save(likeVideo);

            //when
            likeVideoController.deleteLikeVideo(member, likeVideo.getId());

            //then
            assertThat(likeVideoRepository.findAll()).hasSize(0);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 좋아요 삭제 요청")
        void deleteLikeVideo_notExistLikeVideoException_success() {
            //when, then
            assertThrows(
                    NotExistLikeVideoException.class,
                    () -> likeVideoController.deleteLikeVideo(member, 19329283L)
            );
        }

        @Test
        @DisplayName("[실패] 본인이 생성하지 않은 좋아요 삭제 요청")
        void deleteLikeVideo_notMemberOfLikeVideoException_success() {
            //when
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, video);
            likeVideoRepository.save(likeVideo);

            Member testMember = Member.builder()
                    .role(Role.MEMBER).name("테스트유저").emailVerified(true)
                    .build();

            //when, then
            assertThrows(
                    NotMemberOfLikeVideoException.class,
                    () -> likeVideoController.deleteLikeVideo(testMember, video.getId())
            );
        }
    }
}
