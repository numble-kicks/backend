package numble.team4.shortformserver.notification.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.event.domain.FcmEventDomain;
import numble.team4.shortformserver.notification.domain.FcmMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    public final FirebaseMessaging instance;
    private final FcmTokenService fcmTokenService;

    public void sendMessage(Long memberId, Long destinationId, FcmEventDomain domain) {
        Message message = FcmMessage.makeFcmMessage(memberId, domain, fcmTokenService.getFcmToken(destinationId));
        instance.sendAsync(message);
    }

}
