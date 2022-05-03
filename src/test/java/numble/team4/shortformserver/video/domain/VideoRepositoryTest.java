package numble.team4.shortformserver.video.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.ArrayList;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MemberRepository memberRepository;

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

    @Test
    @DisplayName("Video 수정 - 성공")
    void updateVideo_success() throws Exception {
        // given
        Video savedVideo = videoRepository.save(video);
        Video findVideo = videoRepository.findById(savedVideo.getId()).orElseThrow(NotExistVideoException::new);

        Video updateVideo = Video.builder()
            .title("Title update")
            .description("description updated")
            .build();

        // when
        findVideo.update(updateVideo);

        // then
        assertThat(findVideo.getTitle()).isEqualTo(updateVideo.getTitle());
        assertThat(findVideo.getDescription()).isEqualTo(updateVideo.getDescription());
    }
}