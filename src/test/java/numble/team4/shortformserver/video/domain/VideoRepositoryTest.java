package numble.team4.shortformserver.video.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.ArrayList;
import java.util.List;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.dto.VideoUpdateRequest;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
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
    class SaveVideo {

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
            savedVideo.addVideoToMember(member);

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
    class InquiryVideo {

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

        @Test
        @DisplayName("영상 목록 좋아요 순으로 정렬")
        void sortByLikeCounts() throws Exception {
            // given
            List<Video> mockVideos = makeMockVideoList(member);

            videoRepository.saveAll(mockVideos);
            // when
            List<Video> videos = videoRepository.findAllSortByLikeCount(PageRequest.of(0, 10));

            // then
            assertThat(videos.get(0).getLikeCount()).isEqualTo(500L);
            assertThat(videos.get(4).getLikeCount()).isEqualTo(100L);
            assertThat(videos.get(0)).isEqualTo(mockVideos.get(0));
        }

        @Test
        @DisplayName("영상 목록 조회 순으로 정렬")
        void sortByHits() throws Exception {
            // given
            List<Video> mockVideos = makeMockVideoList(member);

            videoRepository.saveAll(mockVideos);
            // when
            List<Video> videos = videoRepository.findAllSortByViewCount(PageRequest.of(0, 10));


            // then
            assertThat(videos.get(0).getViewCount()).isEqualTo(500L);
            assertThat(videos.get(4).getViewCount()).isEqualTo(100L);
            assertThat(videos.get(0)).isEqualTo(mockVideos.get(4));
        }

        @Test
        @DisplayName("특정 사용자의 영상 목록 조회")
        void findAllByMember() throws Exception {
            // given
            Member someMember = Member.builder()
                .name("tester")
                .build();
            Member tester = memberRepository.save(someMember);

            List<Video> mockVideos = makeMockVideoList(tester);
            videoRepository.saveAll(mockVideos);

            // when
            List<Video> videos = videoRepository.findAllByMember(tester, PageRequest.of(0, 10));

            // then
            assertThat(videos)
                .extracting("member")
                .extracting("name")
                .containsExactly("tester", "tester", "tester", "tester", "tester");
        }

        private List<Video> makeMockVideoList(Member member) {
            List<Video> ret = new ArrayList<>();
            Long likeCount = 500L;
            Long viewCount = 100L;
            for (int i = 0; i < 5; i++) {
                ret.add(Video.builder()
                    .title("제목")
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
    class UpdateVideo {

        @Test
        @DisplayName("Video 수정 - 성공")
        void updateVideo_success() throws Exception {
            // given
            Video savedVideo = videoRepository.save(video);
            savedVideo.addVideoToMember(member);
            Video findVideo = videoRepository.findById(savedVideo.getId())
                .orElseThrow(NotExistVideoException::new);

            VideoUpdateRequest videoUpdateRequest = VideoUpdateRequest.builder()
                .title("Title update")
                .description("description updated")
                .build();

            // when
            findVideo.update(videoUpdateRequest.getTitle(), videoUpdateRequest.getDescription(),
                member);
            testEntityManager.flush();
            testEntityManager.clear();

            Member findMember = memberRepository.findById(findVideo.getMember().getId())
                .orElseThrow(NotExistMemberException::new);
            findVideo.update(videoUpdateRequest.getTitle(), videoUpdateRequest.getDescription(),
                member);

            // then
            assertThat(findVideo.getTitle()).isEqualTo(videoUpdateRequest.getTitle());
            assertThat(findVideo.getDescription()).isEqualTo(videoUpdateRequest.getDescription());
            assertThat(findMember.getVideos().get(0).getTitle()).isEqualTo(findVideo.getTitle());
        }

        @Test
        @DisplayName("영상을 수정하면 자동으로 수정 날짜가 주입된다.")
        void updatedVideoWithModifiedAt_success() throws Exception {
            // given
            Video savedVideo = videoRepository.save(video);

            // when
            savedVideo.update("수정", "수정", member);
            testEntityManager.flush();
            testEntityManager.clear();

            // then
            assertThat(savedVideo.getCreateAt()).isBefore(savedVideo.getModifiedAt());
        }
    }

    @Nested
    @DisplayName("Video 삭제 테스트")
    class DeleteVideo {

        @Test
        @DisplayName("Video 삭제 - 성공")
        void deleteVideo_success() throws Exception {
            // given
            Video savedVideo = videoRepository.save(video);
            savedVideo.addVideoToMember(member);

            Video findVideo = videoRepository.findById(savedVideo.getId())
                .orElseThrow(NotExistVideoException::new);

            // when
            videoRepository.delete(findVideo);
            testEntityManager.flush();
            testEntityManager.clear();

            Member findMember = memberRepository.findById(findVideo.getMember().getId())
                .orElseThrow(NotExistMemberException::new);

            // then
            assertThat(findMember.getVideos()).hasSize(0);

            assertThrows(NotExistVideoException.class,
                () -> videoRepository.findById(findVideo.getId())
                    .orElseThrow(NotExistVideoException::new));
        }
    }
}