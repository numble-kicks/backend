package numble.team4.shortformserver.video.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@BaseDataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;
    private Video video;

    private static final String VIDEO_URL = "https://d659rm6fgd091.cloudfront.net/test.mov";
    private static final String THUMBNAIL_URL = "https://d659rm6fgd091.cloudfront.net/test.png";

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .role(Role.MEMBER)
            .videos(new ArrayList<>())
            .build();
        memberRepository.save(member);

        video = Video.builder()
            .title("Title")
            .description("description")
            .member(member)
            .videoUrl(VIDEO_URL)
            .thumbnailUrl(THUMBNAIL_URL)
            .likeCount(0L)
            .viewCount(0L)
            .build();
    }

    //== video 저장 ==//
    @Test
    @DisplayName("video 저장 - 성공")
    public void saveVideo() throws Exception {
        // when
        Video savedVideo = videoRepository.save(video);

        // then
        assertThat(savedVideo.getId()).isNotNull();
        assertThat(savedVideo).isEqualTo(video);
        assertThat(savedVideo.getCreateAt()).isNotNull();
        assertThat(savedVideo.getModifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("Member와 Video 연관관계 매핑 테스트")
    void saveVideoAndMember() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);
        testEntityManager.flush();
        testEntityManager.clear();

        // when
        Video findVideo = videoRepository.findById(savedVideo.getId())
            .orElseThrow(NotExistVideoException::new);
        Member findMember = memberRepository.findById(findVideo.getMember()
            .getId()).orElseThrow(NotExistMemberException::new);

        // then
        assertThat(findVideo.getMember()).isEqualTo(this.member);
        assertThat(findMember.getVideos().get(0)).isEqualTo(findVideo);
    }

    //== video 조희 ==//
    @Test
    @DisplayName("Video id로 조회 - 성공")
    void findById_success() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);

        // when
        Video findVideo = videoRepository.findById(savedVideo.getId()).orElseThrow(NotExistVideoException::new);

        // then
        assertThat(savedVideo).isEqualTo(findVideo);
    }

    @Test
    @DisplayName("Video id로 조회 - 실패, db에 존재하지 않는 영상은 검색할 수 없다.")
    void findById_fail() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);
        Video mockVideo = Video.builder().id(100L).build();

        // when, then
        assertThrows(NotExistVideoException.class,
            () -> videoRepository.findById(mockVideo.getId()).orElseThrow(NotExistVideoException::new));
    }

    //== video 수정 ==//
    @Test
    @DisplayName("Video 수정 - 성공")
    void updateVideo_success() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);
        testEntityManager.flush();
        testEntityManager.clear();

        Video findVideo = videoRepository.findById(savedVideo.getId()).orElseThrow(NotExistVideoException::new);

        // when
        findVideo.update("제목 수정", "설명 수정", member);
        testEntityManager.flush();
        testEntityManager.clear();

        Member findMember = memberRepository.findById(findVideo.getMember().getId())
            .orElseThrow(NotExistMemberException::new);
        findVideo.update("제목 수정", "설명 수정", member);

        // then
        assertThat(findVideo.getTitle()).isEqualTo("제목 수정");
        assertThat(findVideo.getDescription()).isEqualTo("설명 수정");
        assertThat(findMember.getVideos().get(0).getTitle()).isEqualTo(findVideo.getTitle());
    }

    //== video 삭제 ==//
    @Test
    @DisplayName("Video 삭제 - 성공")
    void deleteVideo_success() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);
        testEntityManager.flush();
        testEntityManager.clear();

        Video findVideo = videoRepository.findById(savedVideo.getId()).orElseThrow(NotExistVideoException::new);

        // when
        videoRepository.delete(findVideo);
        testEntityManager.flush();
        testEntityManager.clear();

        Member findMember = memberRepository.findById(findVideo.getMember().getId())
            .orElseThrow(NotExistMemberException::new);

        // then
        assertThat(findMember.getVideos()).hasSize(0);

        assertThrows(NotExistVideoException.class,
            () -> videoRepository.findById(findVideo.getId()).orElseThrow(NotExistVideoException::new));
    }
}