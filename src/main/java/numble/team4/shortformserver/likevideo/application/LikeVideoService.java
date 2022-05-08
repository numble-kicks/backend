package numble.team4.shortformserver.likevideo.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.likevideo.domain.LikeVideo;
import numble.team4.shortformserver.likevideo.domain.LikeVideoRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.domain.VideoRepository;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeVideoService {

    private final LikeVideoRepository likeVideoRepository;
    private final VideoRepository videoRepository;

    public void saveLikeVideo(Member member, Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(NotExistVideoException::new);

        LikeVideo likeVideo = LikeVideo.fromMemberAndVideo(member, video);
        likeVideoRepository.save(likeVideo);
    }
}
