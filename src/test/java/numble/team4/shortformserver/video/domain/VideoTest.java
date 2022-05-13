package numble.team4.shortformserver.video.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VideoTest {

    @Test
    @DisplayName("영상을 조회하면 조회 수가 증가해야 한다.")
    void increaseHits() throws Exception {
        // given
        Video video = Video.builder()
            .viewCount(0L)
            .build();

        // when
        video.increaseViewCount();

        // then
        assertThat(video.getViewCount()).isEqualTo(1L);
    }
}