package numble.team4.shortformserver.video.infrastructure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.likevideo.domain.QLikeVideo;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;
import numble.team4.shortformserver.video.dto.QVideoListResponse;
import numble.team4.shortformserver.video.dto.VideoListResponse;

import java.util.List;
import java.util.Objects;

import static numble.team4.shortformserver.likevideo.domain.QLikeVideo.likeVideo;
import static numble.team4.shortformserver.video.domain.QVideo.video;

@RequiredArgsConstructor
public class VideoCustomRepositoryImpl implements VideoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Video> findAllByMemberAndMaxVideoId(Member member, Long maxVideoId, int limitNum) {

        return jpaQueryFactory.selectFrom(video)
                .orderBy(video.id.desc())
                .where(videoIdIsLessThan(maxVideoId))
                .limit(limitNum)
                .fetch();
    }

    public List<Video> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long maxVideoId, int limitNum) {

        return jpaQueryFactory.selectFrom(video)
                .join(likeVideo)
                .on(likeVideo.member.eq(member).and(likeVideo.video.id.eq(video.id)))
                .orderBy(video.id.desc())
                .where(videoIdIsLessThan(maxVideoId))
                .limit(limitNum)
                .fetch();
    }

    public BooleanBuilder videoIdIsLessThan(Long videoId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (Objects.nonNull(videoId)) {
            builder.and(video.id.lt(videoId));
        }

        return builder;
    }
}
