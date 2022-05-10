package numble.team4.shortformserver.follow.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FollowResponseMessage {
    CREATE_FOLLOW("팔로우 생성 성공"),
    DELETE_FOLLOW("팔로우 취소 성공"),
    GET_FOLLOWERS("팔로워 목록 조회 성공"),
    GET_FOLLOWINGS("팔로잉 목록 조회 성공");

    private final String message;
}
