package numble.team4.shortformserver.video.infrastructure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.dto.QVideoListResponse;
import numble.team4.shortformserver.video.dto.VideoListResponse;

import java.util.List;
import java.util.Objects;

import static numble.team4.shortformserver.video.domain.QVideo.video;

@RequiredArgsConstructor
public class VideoCustomRepositoryImpl implements VideoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<VideoListResponse> findAllByMemberAndMaxVideoId(Member member, Long videoId, int limitNum) {

        return jpaQueryFactory.select(new QVideoListResponse(
                        video.id, video.thumbnailUrl))
                .from(video)
                .orderBy(video.id.desc())
                .where(videoIdIsLessThan(videoId))
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