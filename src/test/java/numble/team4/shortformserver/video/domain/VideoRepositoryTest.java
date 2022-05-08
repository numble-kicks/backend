package numble.team4.shortformserver.video.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@BaseDataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private static final String VIDEO_URL = "https://d659rm6fgd091.cloudfront.net/test.mov";
    private static final String THUMBNAIL_URL = "https://d659rm6fgd091.cloudfront.net/test.png";

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .role(Role.MEMBER)
            .videos(new ArrayList<>())
            .build();
    }

    @Test
    @DisplayName("video 저장 - 성공")
    public void saveVideo() throws Exception {
        // given
        memberRepository.save(member);

        Video video = Video.builder()
            .title("Title")
            .description("description")
            .member(member)
            .videoUrl(VIDEO_URL)
            .thumbnailUrl(THUMBNAIL_URL)
            .likeCount(0L)
            .viewCount(0L)
            .build();

        // when
        Video savedVideo = videoRepository.save(video);

        // then
        assertThat(savedVideo.getId()).isNotNull();
        assertThat(savedVideo).isEqualTo(video);
        assertThat(savedVideo.getCreateAt()).isNotNull();
        assertThat(savedVideo.getModifiedAt()).isNotNull();
    }
}