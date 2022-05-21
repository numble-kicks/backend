package numble.team4.shortformserver.notification.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.notification.application.FcmTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static numble.team4.shortformserver.notification.ui.FcmTokenResponseMessage.SAVE_FCM_TOKEN;

@RestController
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @PostMapping("/fcm-token")
    public CommonResponse saveFcmToken(@LoginUser Member member, @RequestHeader("fcm-token") String fcm) {
        fcmTokenService.saveFcmToken(member, fcm);
        return CommonResponse.from(SAVE_FCM_TOKEN.getMessage());
    }
}
