package numble.team4.shortformserver.video.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VideoCustomRepositoryImpl implements VideoCustomRepository {

    private final JPAQueryFactory factory;
}
