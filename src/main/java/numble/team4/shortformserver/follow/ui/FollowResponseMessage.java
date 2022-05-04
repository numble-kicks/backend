package numble.team4.shortformserver.follow.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowResponseMessage {
    CREATE_FOLLOW("팔로우 생성 성공");

    private final String message;
}
