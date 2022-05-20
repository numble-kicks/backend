package numble.team4.shortformserver.event.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FcmTokenPushingEvent extends ApplicationEvent {

    private Long currentMemberId;
    private Long destinationMemberId;
    private FcmEventDomain domain;

    public FcmTokenPushingEvent(Object source, Long currentMemberId, Long destinationMemberId, FcmEventDomain domain) {
        super(source);
        this.currentMemberId = currentMemberId;
        this.destinationMemberId = destinationMemberId;
        this.domain = domain;
    }
}
