package numble.team4.shortformserver.video.infrastructure;


import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

import java.util.List;

public interface VideoCustomRepository {

    List<Video> findAllByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);
    List<Video> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);

}
