package numble.team4.shortformserver.event.util;

import numble.team4.shortformserver.event.domain.FcmEventDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@SpringBootTest
class FcmEventPublisherTest {

    @Autowired
    FcmEventPublisher publisher;

    @Test
    public void test() {
        publisher.publishFcmTokenPushingEvent(12L, 13L, FcmEventDomain.FOLLOW);
    }

}