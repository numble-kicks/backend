package numble.team4.shortformserver.event.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.event.domain.FcmEventDomain;
import numble.team4.shortformserver.event.domain.FcmTokenPushingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishFcmTokenPushingEvent(Long currentMemberId, Long destinationMemberId, FcmEventDomain domain) {
        ApplicationEvent event = new FcmTokenPushingEvent(this, currentMemberId, destinationMemberId, domain);
        applicationEventPublisher.publishEvent(event);
    }
}
