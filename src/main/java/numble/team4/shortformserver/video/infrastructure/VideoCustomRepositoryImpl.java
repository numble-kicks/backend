package numble.team4.shortformserver.video.infrastructure;

import static com.querydsl.core.types.Order.DESC;
import static com.querydsl.core.types.dsl.StringExpressions.lpad;
import static numble.team4.shortformserver.video.domain.QVideo.video;
import static org.springframework.util.StringUtils.hasText;
import static numble.team4.shortformserver.likevideo.domain.QLikeVideo.likeVideo;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


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
    public List<Video> getTopVideos(String sortBy, Integer limitNum) {
        return factory
            .selectFrom(video)
            .orderBy(videoSort(sortBy), video.id.desc())
            .limit(limitNum)
            .fetch();
    }

    @Override
    public List<Video> findAllByMemberAndMaxVideoId(Member member, Long maxVideoId, int limitNum) {

        return factory.selectFrom(video)
            .orderBy(video.id.desc())
            .where(videoIdIsLessThan(maxVideoId))
            .limit(limitNum)
            .fetch();
    }

    public List<Video> findAllLikeVideoByMemberAndMaxVideoId(Member member, Long maxVideoId, int limitNum) {

        return factory.selectFrom(video)
            .join(likeVideo)
            .on(likeVideo.member.eq(member).and(likeVideo.video.id.eq(video.id)))
            .orderBy(video.id.desc())
            .where(videoIdIsLessThan(maxVideoId))
            .limit(limitNum)
            .fetch();
    }

    @Override
    public Page<Video> getAllVideos(Pageable page, Long total, Long memberId) {
        List<Video> videos = factory
            .selectFrom(video)
            .where(existMemberId(memberId))
            .orderBy(video.id.asc())
            .offset(page.getOffset())
            .limit(page.getPageSize())
            .fetch();

        return new PageImpl<>(videos, page, total);
    }

    private Predicate existMemberId(Long memberId) {
        if (Objects.isNull(memberId)) {
            return null;
        }

        return video.member.id.eq(memberId);
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

    public BooleanBuilder videoIdIsLessThan(Long videoId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (Objects.nonNull(videoId)) {
            builder.and(video.id.lt(videoId));
        }

        return builder;
    }

}
