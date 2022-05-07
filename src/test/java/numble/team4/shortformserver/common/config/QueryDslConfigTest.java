package numble.team4.shortformserver.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@BaseDataJpaTest
class QueryDslConfigTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void test() {
        returnFactory();
    }

    JPAQueryFactory returnFactory() {
        return jpaQueryFactory;
    }
}