package numble.team4.shortformserver.video.infrastructure;

import numble.team4.shortformserver.video.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoCustomRepository {

    Page<Video> findAllVideos(String cursor, String sortBy, Pageable pageable);
}
