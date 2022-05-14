package numble.team4.shortformserver.video.infrastructure;

import java.util.List;
import numble.team4.shortformserver.video.domain.Video;

public interface VideoCustomRepository {

    List<Video> searchVideoByKeyword(Long lastId, String keyword, String sortBy);

    List<Video> getTopVideo(String sortBy, Integer limitNum);
}
