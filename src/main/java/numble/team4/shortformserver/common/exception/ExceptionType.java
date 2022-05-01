package numble.team4.shortformserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.exception.*;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    NOT_EXIST_MEMBER("존재하지 않는 회원입니다.", BAD_REQUEST, NotExistMemberException.class),
    NOT_EXIST_VIDEO("존재하지 않는 영상입니다.", BAD_REQUEST, NotExistVideoException.class),
    WRONG_PASSWORD("비밀번호가 틀렸습니다.", BAD_REQUEST, WrongPasswordException.class),
    KAKAO_LOGIN_FAIL("카카오 로그인 실패했습니다.", BAD_REQUEST, KakaoLoginFailException.class),
    JSON_PARSE_FAIL("유저 정보를 파싱하는 과정에서 예외가 발생했습니다.", INTERNAL_SERVER_ERROR, JsonParsingException.class),
    EMAIL_EMPTY("유저의 이메일이 없습니다.", UNAUTHORIZED, EmailEmptyException.class),
    NOT_EXIST_PROVIDER("존재하지 않는 소셜로그인 제공사입니다.", BAD_REQUEST, NotExistProviderException.class),
    JWT_TOKEN_EXPIRED("토큰이 만료되었습니다.", UNAUTHORIZED, JwtTokenExpiredException.class);

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
