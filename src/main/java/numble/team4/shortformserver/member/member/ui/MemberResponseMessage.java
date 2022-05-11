package numble.team4.shortformserver.member.member.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResponseMessage {

    GET_ALL_VIDEOS_OF_MEMBER("회원의 영상 목록 조회 성공");

    private final String message;
}
