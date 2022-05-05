package numble.team4.shortformserver.member.auth.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthResponseMessage {
    LOGIN_SUCCESS("로그인 성공"),
    TOKEN_RENEW_SUCCESS("토큰 재발급 성공");

    private final String message;
}
