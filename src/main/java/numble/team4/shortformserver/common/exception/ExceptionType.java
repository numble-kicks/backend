package numble.team4.shortformserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.member.auth.exception.WrongPasswordException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NOT_EXIST_MEMBER("존재하지 않는 회원입니다.", BAD_REQUEST, NotExistMemberException.class),
    NOT_EXIST_VIDEO("존재하지 않는 영상입니다.", BAD_REQUEST, NotExistVideoException.class),
    WRONG_PASSWORD("비밀번호가 틀렸습니다.", BAD_REQUEST, WrongPasswordException.class),
    ALREADY_EXIST_FOLLOW("이미 팔로우가 되어있는 사용자입니다.", BAD_REQUEST, AlreadyExistFollowException.class),
    NOT_SELF_FOLLOW_ABLE("자기 자신은 팔로우 할 수 없습니다.", BAD_REQUEST, NotSelfFollowableException.class);

    private final String message;
    private final HttpStatus status;
    private final Class<? extends Exception> type;

    public static ExceptionType findException(Class<? extends Exception> type) {
        return Arrays.stream(ExceptionType.values())
                .filter(exception -> exception.getType().equals(type))
                .findAny()
                .orElseThrow(UnexpectedException::new);
    }
}
