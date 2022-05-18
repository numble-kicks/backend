package numble.team4.shortformserver.video.infrastructure;


import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoCustomRepository {

    List<Video> findAllByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);

    List<Video> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);

    List<Video> searchVideoByKeyword(Long lastId, String keyword, String sortBy);

    List<Video> getTopVideos(String sortBy, Integer limitNum);

    Page<Video> getAllVideos(Pageable page, Long total, Long memberId);
}
