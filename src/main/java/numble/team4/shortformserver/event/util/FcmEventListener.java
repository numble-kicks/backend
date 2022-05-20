package numble.team4.shortformserver.event.util;

import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.event.domain.FcmTokenPushingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmEventListener implements ApplicationListener<FcmTokenPushingEvent> {

    @Override
    public void onApplicationEvent(FcmTokenPushingEvent event) {
        log.info("generate Fcm token pushing event");
    }
}
