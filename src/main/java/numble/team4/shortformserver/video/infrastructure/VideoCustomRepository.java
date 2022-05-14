package numble.team4.shortformserver.video.infrastructure;

import java.util.List;
import numble.team4.shortformserver.video.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoCustomRepository {

    List<Video> searchVideoByKeyword(Long lastId, String keyword, String sortBy);

    List<Video> getVideoTop10(String sortBy);

    Page<Video> getAllVideo(Pageable page, Long total);
}
