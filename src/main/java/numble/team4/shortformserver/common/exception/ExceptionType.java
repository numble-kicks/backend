package numble.team4.shortformserver.common.exception;

import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.aws.exception.AmazonClientException;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.member.auth.exception.WrongPasswordException;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import numble.team4.shortformserver.member.auth.exception.NotLoggedInException;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NOT_EXIST_MEMBER("존재하지 않는 회원입니다.", BAD_REQUEST, NotExistMemberException.class),
    NOT_EXIST_VIDEO("존재하지 않는 영상입니다.", BAD_REQUEST, NotExistVideoException.class),
    WRONG_PASSWORD("비밀번호가 틀렸습니다.", BAD_REQUEST, WrongPasswordException.class),
    ALREADY_EXIST_FOLLOW("이미 팔로우가 되어있는 사용자입니다.", BAD_REQUEST, AlreadyExistFollowException.class),
    NOT_SELF_FOLLOW_ABLE("본인은 팔로우 할 수 없습니다.", BAD_REQUEST, NotSelfFollowableException.class),
    NOT_EXIST_FILE("존재하지 않는 파일입니다.", BAD_REQUEST, NotExistFileException.class),
    AMAZON_CLIENT_EXCEPTION("아마존 서버에 업로드하는 과정에서 오류가 발생하였습니다.", INTERNAL_SERVER_ERROR, AmazonClientException.class),
    NOT_LOGGED_IN("로그인이 되어있지 않습니다.", UNAUTHORIZED, NotLoggedInException.class),
    NOT_AUTHOR_EXCEPTION("작성자가 아니므로 권한이 없습니다.",UNAUTHORIZED, NotAuthorException.class);



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
