package numble.team4.shortformserver.notification.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FcmTokenResponseMessage {

    SAVE_FCM_TOKEN("파이어베이스 메시징 토큰 저장 성공");

    private final String message;
}
