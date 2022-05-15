package numble.team4.shortformserver.video.infrastructure;

import java.util.List;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoCustomRepository {

    List<Video> searchVideoByKeyword(Long lastId, String keyword, String sortBy);

    List<Video> findAllByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);

    List<Video> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);

    List<Video> getTopVideos(String sortBy, Integer limitNum);

    Page<Video> getAllVideos(Pageable page, Long total);
}
