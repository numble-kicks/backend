package numble.team4.shortformserver.notification.application;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.event.domain.FcmEventDomain;
import numble.team4.shortformserver.notification.domain.FcmMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmPushService {

    public final FirebaseMessaging instance;
    private final StringRedisTemplate stringRedisTemplate;

    public void sendMessage(Long memberId, Long destinationId, FcmEventDomain domain) {
        Message message = FcmMessage.makeFcmMessage(memberId, domain, getFcmToken(destinationId));
        instance.sendAsync(message);
    }

    private String getFcmToken(Long memberId) {
        return stringRedisTemplate.opsForValue().get(String.valueOf(memberId));
    }

}
