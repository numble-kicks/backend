package numble.team4.shortformserver.event.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.event.domain.FcmTokenPushingEvent;
import numble.team4.shortformserver.notification.application.NotificationService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmEventListener implements ApplicationListener<FcmTokenPushingEvent> {

    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(FcmTokenPushingEvent event) {
        log.info("generate Fcm token pushing event");
        notificationService.sendMessage(event.getCurrentMemberId(), event.getDestinationMemberId(), event.getDomain());
    }
}
