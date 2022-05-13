package numble.team4.shortformserver.member.integration;

import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.likevideo.domain.LikeVideoRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.ui.MemberController;
import numble.team4.shortformserver.member.member.ui.dto.MemberEmailRequest;
import numble.team4.shortformserver.member.member.ui.dto.MemberInfoResponse;
import numble.team4.shortformserver.member.member.ui.dto.MemberNameUpdateRequest;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BaseIntegrationTest
public class MemberIntegrationTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MemberController memberController;

    @Autowired
    LikeVideoRepository likeVideoRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    AmazonS3Uploader uploader;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder()
                .name("user2").role(Role.MEMBER).emailVerified(true)
                .build();
        memberRepository.save(member);
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
            videoRepository.save(video);
        }
    }
    void createLikeVideo() {
        List<Video> all = videoRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, all.get(i));
            likeVideoRepository.save(likeVideo);
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


    @Test
    @DisplayName("[성공] 존재하는 사용자의 정보 조회")
    void getMemberInfo_notException_success() {
        //when
        MemberInfoResponse res = memberController.findMemberInfo(member.getId()).getData();

        //then
        assertThat(res.getName()).isEqualTo(member.getName());
        assertThat(res.getEmail()).isEqualTo(member.getEmail());
        assertThat(res.getProfileImageUrl()).isEqualTo(member.getProfileImageUrl());
        assertThat(res.getFollowers()).isNotNull();
        assertThat(res.getFollowings()).isNotNull();
        assertThat(res.getVideos()).isNotNull();
    }


    @Test
    @DisplayName("[성공] 사용자 프로필 이미지 등록")
    void saveProfileImage_notExceptionAndMemberProfileImageNotNull_success() throws IOException {
        //given
        entityManager.flush();
        entityManager.clear();
        MockMultipartFile file = new MockMultipartFile("testImage",
                new FileInputStream("src/test/resources/spring.png"));

        //when
        memberController.updateProfileImage(member, file);

        //then
        Member byId = memberRepository.getById(member.getId());
        assertThat(byId.getProfileImageUrl()).isNotNull();

        uploader.deleteToS3(member.getProfileImageUrl());
    }

    @Test
    @DisplayName("[성공] 사용자 닉네임 수정")
    void updateUserName_memberNameModified_fail() {
        //given
        entityManager.flush();
        entityManager.clear();
        MemberNameUpdateRequest request = new MemberNameUpdateRequest("kebin");

        //when
        memberController.updateUserName(member, request);

        //then
        assertThat(memberRepository.getById(this.member.getId()).getName()).isEqualTo("kebin");
    }

    @Test
    @DisplayName("[성공] 사용자 이메일 등록")
    void saveEmail_memberEmailModifiedAndNotNull_success() {
        //given
        entityManager.flush();
        entityManager.clear();
        String testEmail = "test@numble.com";
        MemberEmailRequest request = new MemberEmailRequest(testEmail);

        //when
        memberController.saveEmail(member, request);

        //then
        assertThat(memberRepository.getById(member.getId()).getEmail()).isEqualTo(testEmail);
    }


}
