package numble.team4.shortformserver.video.infrastructure;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.dto.VideoListResponse;

import java.util.List;

public interface VideoCustomRepository {

    List<VideoListResponse> findAllByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);
    List<VideoListResponse> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long videoId, int limitNum);
}
