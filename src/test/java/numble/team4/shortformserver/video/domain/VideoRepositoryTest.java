package numble.team4.shortformserver.video.domain;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

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
            .role(MEMBER)
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

    @Nested
    @DisplayName("Video 저장 테스트")
    class SaveVideoTest {

        @Test
        @DisplayName("Video를 저장한다.")
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
        @DisplayName("Member, Video 연관관계 매핑 테스트")
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
            assertThat(findVideo.getMember()).isEqualTo(member);
            assertThat(findMember.getVideos().get(0)).isEqualTo(findVideo);
        }

    }

    @Nested
    @DisplayName("Video 조회 테스트")
    class InquiryVideoTest {

        @Test
        @DisplayName("Video id로 조회(특정 영상 조회)")
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

        @Test
        @DisplayName("영상 목록 최신순으로 정렬")
        void sortByLatest() throws Exception {
            // given
            List<Video> mockVideos = makeMockVideoList(member);
            videoRepository.saveAll(mockVideos);

            // when
            List<Video> videos = videoRepository.findAll(Sort.by("createAt").descending());

            // then
            assertThat(videos.get(0)).isEqualTo(mockVideos.get(4));
        }

        private List<Video> makeMockVideoList(Member member) {
            List<Video> ret = new ArrayList<>();
            Long likeCount = 500L;
            Long viewCount = 100L;
            for (int i = 0; i < 5; i++) {
                ret.add(Video.builder()
                    .title(String.valueOf(i + 1))
                    .videoUrl("영상")
                    .thumbnailUrl("썸네일")
                    .member(member)
                    .likeCount(likeCount)
                    .viewCount(viewCount)
                    .build()
                );
                likeCount -= 100L;
                viewCount += 100L;
            }
            return ret;
        }
    }

    @Nested
    @DisplayName("Video 수정")
    class UpdateVideoTest {

        @Test
        @DisplayName("Video 수정 - 성공")
        void updateVideo_success() throws Exception {
            // given
            Video savedVideo = videoRepository.save(video);

            // when
            savedVideo.update("제목 수정", "설명 수정");
            testEntityManager.flush();
            testEntityManager.clear();

            Member findMember = memberRepository.findById(savedVideo.getMember().getId())
                .orElseThrow(NotExistMemberException::new);

            // then
            assertThat(savedVideo.getTitle()).isEqualTo("제목 수정");
            assertThat(savedVideo.getDescription()).isEqualTo("설명 수정");
            assertThat(savedVideo.getCreateAt()).isBefore(savedVideo.getModifiedAt());
        }
    }

    @Nested
    @DisplayName("Video 삭제 테스트")
    class DeleteVideoTest {

        @Test
        @DisplayName("Video 삭제 - 성공")
        void deleteVideo_success() throws Exception {
            // given
            Video savedVideo = videoRepository.save(video);

            // when
            videoRepository.delete(savedVideo);
            testEntityManager.flush();
            testEntityManager.clear();

            Member findMember = memberRepository.findById(savedVideo.getMember().getId())
                .orElseThrow(NotExistMemberException::new);

            // then
            assertThat(findMember.getVideos()).isEmpty();

            assertThrows(NotExistVideoException.class,
                () -> videoRepository.findById(savedVideo.getId())
                    .orElseThrow(NotExistVideoException::new));
        }
    }
}