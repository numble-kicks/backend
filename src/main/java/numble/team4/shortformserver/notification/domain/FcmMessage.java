package numble.team4.shortformserver.notification.domain;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.event.domain.FcmEventDomain;

@RequiredArgsConstructor
public class FcmMessage {

    public static Message makeFcmMessage(Long memberId, FcmEventDomain domain, String fcmToken) {
        WebpushNotification notification = WebpushNotification.builder()
                .setTitle(domain.getTitle())
                .setBody(domain.getBody(memberId))
                .build();

        WebpushConfig config = WebpushConfig.builder()
                .setNotification(notification)
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setWebpushConfig(config)
                .build();
        return message;
    }
}
