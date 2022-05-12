package numble.team4.shortformserver.video.infrastructure;

import java.util.List;
import numble.team4.shortformserver.video.domain.Video;
import org.springframework.data.domain.Pageable;

public interface VideoCustomRepository {

    List<Video> findAllVideos(String cursor, String sortBy, Pageable pageable);
}
