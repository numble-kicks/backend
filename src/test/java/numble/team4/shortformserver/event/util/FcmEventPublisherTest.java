package numble.team4.shortformserver.event.util;

import numble.team4.shortformserver.event.domain.FcmEventDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest
class FcmEventPublisherTest {

    @Autowired
    FcmEventPublisher publisher;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void init() {
        stringRedisTemplate.opsForValue().set("13", "testFcmToken");
    }

    @AfterEach
    void close() {
        stringRedisTemplate.delete("13");
    }

    @Test
    public void test() {
        publisher.publishFcmTokenPushingEvent(12L, 13L, FcmEventDomain.FOLLOW);
    }

}