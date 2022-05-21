package numble.team4.shortformserver.notification.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final StringRedisTemplate stringRedisTemplate;

    public void saveFcmToken(Member member, String fcmToken) {
        stringRedisTemplate.opsForValue().set(String.valueOf(member.getId()), fcmToken);
    }

    public String getFcmToken(Long memberId) {
        return stringRedisTemplate.opsForValue().get(String.valueOf(memberId));
    }

    public void deleteFcmToken(Member member) {
        stringRedisTemplate.delete(String.valueOf(member.getId()));
    }
}
