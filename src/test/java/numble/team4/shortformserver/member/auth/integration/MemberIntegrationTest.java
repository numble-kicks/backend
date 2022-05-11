package numble.team4.shortformserver.member.auth.integration;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.member.member.ui.MemberController;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                    .description("description")
                    .build();
            entityManager.persist(video);
        }
    }

    @Nested
    @DisplayName("마이비디오 목록 조회")
    class GetMyVideosTest {

        @Test
        @DisplayName("[성공] 마이비디오 목록 조회 (videoId가 null일 때)")
        void findAllVideosByMember_returnListHasSize18_success() {
            //when
            List<VideoListResponse> data = memberController.findAllVideosByMember(member.getId(), null).getData();

            //then
            assertThat(data).hasSize(18);
            for (int i = 0; i < 14; i++) {
                assertTrue(data.get(i).getId() > data.get(i + 1).getId());
            }
        }

        @Test
        @DisplayName("[성공] 마이비디오 목록 조회 (videoId가 null이 아닐 때)")
        void findAllVideosByMember_returnListHasSize2_success() {
            //given
            Long videoId = videoRepository.findAll()
                    .stream()
                    .map(x -> x.getId())
                    .sorted()
                    .collect(Collectors.toList()).get(5);
            //when
            List<VideoListResponse> data = memberController.findAllVideosByMember(member.getId(), videoId).getData();

            //then
            assertThat(data).hasSize(5);
            for (int i = 0; i < 4; i++) {
                assertTrue(data.get(i).getId() > data.get(i + 1).getId());
            }
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 멤버일 때")
        void findAllVideosByMember_notExistMemberException_fail() {
            //when,then

            assertThrows(
                    NotExistMemberException.class,
                    () -> memberController.findAllVideosByMember(99999L, null)
            );

        }
    }
}
