package numble.team4.shortformserver.event.domain;

import org.springframework.context.ApplicationEvent;

public class FcmTokenPushingEvent extends ApplicationEvent {

    private Long memberId;
    private FcmEventDomain domain;

    public FcmTokenPushingEvent(Object source, Long memberId, FcmEventDomain domain) {
        super(source);
        this.memberId = memberId;
        this.domain = domain;
    }
}
