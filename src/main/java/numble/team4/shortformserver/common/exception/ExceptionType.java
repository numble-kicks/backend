package numble.team4.shortformserver.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.aws.exception.AmazonClientException;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.member.auth.exception.WrongPasswordException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NOT_EXIST_MEMBER("존재하지 않는 회원입니다.", BAD_REQUEST, NotExistMemberException.class),
    NOT_EXIST_VIDEO("존재하지 않는 영상입니다.", BAD_REQUEST, NotExistVideoException.class),
    WRONG_PASSWORD("비밀번호가 틀렸습니다.", BAD_REQUEST, WrongPasswordException.class),
    NOT_EXIST_FILE("존재하지 않는 파일입니다.", BAD_REQUEST, NotExistFileException.class),
    AMAZON_CLIENT_EXCEPTION("아마존 서버에 업로드하는 과정에서 오류가 발생하였습니다.", INTERNAL_SERVER_ERROR, AmazonClientException.class);

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
