package numble.team4.shortformserver.video.infrastructure;

import static com.querydsl.core.types.Order.DESC;
import static com.querydsl.core.types.dsl.StringExpressions.lpad;
import static numble.team4.shortformserver.video.domain.QVideo.video;
import static org.springframework.util.StringUtils.hasText;
import static numble.team4.shortformserver.likevideo.domain.QLikeVideo.likeVideo;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
public class VideoCustomRepositoryImpl implements VideoCustomRepository {

    private static final int LIMIT = 18;
    private final JPAQueryFactory factory;

    @Override
    public List<Video> searchVideoByKeyword(Long lastId, String keyword, String sortBy) {
        return factory
            .selectFrom(video)
            .where(video.description.contains(keyword)
                    .or(video.title.contains(keyword)),
                cursorIsLessThan(sortBy, lastId))
            .orderBy(videoSort(sortBy), video.id.desc())
            .limit(LIMIT)
            .fetch();
    }

    @Override
    public List<Video> getTopVideo(String sortBy, Integer limitNum) {
        return factory
            .selectFrom(video)
            .orderBy(videoSort(sortBy), video.id.desc())
            .limit(limitNum)
            .fetch();
    }

    @Override
    public List<Video> findAllByMemberAndMaxVideoId(Member member, Long maxVideoId, int limitNum) {

        return jpaQueryFactory.selectFrom(video)
            .orderBy(video.id.desc())
            .where(videoIdIsLessThan(maxVideoId))
            .limit(limitNum)
            .fetch();
    }

    private BooleanExpression cursorIsLessThan(String sort, Long cursorId) {
        if (Objects.isNull(cursorId)) {
            return null;
        }

        if (sort.equals("hits")) {
            String cursor =
                String.format("%010d", getHits(cursorId)) + String.format("%010d", cursorId);
            return convertToUniqueValue().lt(cursor);
        }

        return video.id.lt(cursorId);
    }

    /**
     * 조회수가 같다면 같은 조회수는 제외된다. 따라서 조회수와 id를 활용해 공식을 만들어 Unique한 값을 생성하고 비교를 해야된다.
     */
    private StringExpression convertToUniqueValue() {
        return lpad(video.viewCount.stringValue(), 10, '0').concat(
            lpad(video.id.stringValue(), 10, '0'));
    }

    private Long getHits(Long cursorId) {
        return Objects
            .requireNonNull(factory.selectFrom(video).where(video.id.eq(cursorId)).fetchOne())
            .getViewCount();
    }
    private OrderSpecifier<?> videoSort(String sortBy) {
        if (!hasText(sortBy)) {
            return new OrderSpecifier<>(DESC, video.id);
        }
        return new OrderSpecifier<>(DESC,
            (sortBy.equals("hits") ? video.viewCount : video.likeCount));
    }

    private final JPAQueryFactory jpaQueryFactory;

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
