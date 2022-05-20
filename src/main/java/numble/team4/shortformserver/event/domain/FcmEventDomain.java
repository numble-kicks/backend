package numble.team4.shortformserver.event.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FcmEventDomain {
    FOLLOW("팔로우 알림", "님께서 사용자를 팔로우했습니다."),
    CHATTING("채팅 알림", "님께서 채팅을 시작했습니다.");

    private final String title;
    private final String body;

    public String getBody(Long memberId) {
        return memberId + this.body;
    }

}
