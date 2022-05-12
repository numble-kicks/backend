package numble.team4.shortformserver.video.infrastructure;

import static numble.team4.shortformserver.video.domain.QVideo.video;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.video.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class VideoCustomRepositoryImpl implements VideoCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Page<Video> findAllVideos(String cursor, String sortBy, Pageable pageable) {
        List<Video> videos = factory
            .select(video)
            .from(video)
            .orderBy(videoSort(sortBy), video.id.desc())
            .where(cursorId(cursor, sortBy))
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(videos, pageable, pageable.getPageSize());
    }

    private OrderSpecifier<?> videoSort(String sortBy) {
        if (sortBy.equals("hits")) {
            return new OrderSpecifier<>(Order.DESC, video.viewCount);
        }
        if (sortBy.equals("likes")) {
            return new OrderSpecifier<>(Order.DESC, video.likeCount);
        }
        return new OrderSpecifier<>(Order.DESC, video.createAt);
    }

    private BooleanExpression cursorId(String cursor, String sortBy) {
        if (cursor == null) {
            return null;
        } else if (sortBy.equals("hits")) {
            return video.hitsCursor.lt(cursor);
        } else if (sortBy.equals("likes")) {
            return video.likesCursor.lt(cursor);
        }
        return null;
    }
}
