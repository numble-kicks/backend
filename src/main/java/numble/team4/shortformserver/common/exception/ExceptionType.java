package numble.team4.shortformserver.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.aws.exception.AmazonClientException;
import numble.team4.shortformserver.aws.exception.NotExistFileException;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotExistFollowException;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.likevideo.exception.AlreadyExistLikeVideoException;
import numble.team4.shortformserver.likevideo.exception.NotExistLikeVideoException;
import numble.team4.shortformserver.likevideo.exception.NotMemberOfLikeVideoException;
import numble.team4.shortformserver.member.auth.exception.*;
import numble.team4.shortformserver.member.auth.exception.EmailEmptyException;
import numble.team4.shortformserver.member.auth.exception.JsonParsingException;
import numble.team4.shortformserver.member.auth.exception.JwtTokenExpiredException;
import numble.team4.shortformserver.member.auth.exception.KakaoLoginFailException;
import numble.team4.shortformserver.member.auth.exception.NotExistProviderException;
import numble.team4.shortformserver.member.auth.exception.WrongPasswordException;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.video.exception.NotExistVideoException;
import numble.team4.shortformserver.video.exception.NotLoggedInException;
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
    JWT_TOKEN_EXPIRED("토큰이 만료되었습니다.", UNAUTHORIZED, JwtTokenExpiredException.class),
    ALREADY_EXIST_FOLLOW("이미 팔로우가 되어있는 사용자입니다.", BAD_REQUEST, AlreadyExistFollowException.class),
    NOT_SELF_FOLLOW_ABLE("본인은 팔로우 할 수 없습니다.", BAD_REQUEST, NotSelfFollowableException.class),
    NOT_EXIST_FILE("존재하지 않는 파일입니다.", BAD_REQUEST, NotExistFileException.class),
    AMAZON_CLIENT_EXCEPTION("아마존 서버에 업로드하는 과정에서 오류가 발생하였습니다.", INTERNAL_SERVER_ERROR, AmazonClientException.class),
    NOT_LOGGED_IN("로그인이 되어있지 않습니다.", UNAUTHORIZED, NotLoggedInException.class),
    NOT_EXIST_FOLLOW("존재하지 않는 팔로우입니다.", BAD_REQUEST, NotExistFollowException.class),
    NOT_FOLLOWING("본인의 팔로우만 삭제할 수 있습니다.", BAD_REQUEST, NotFollowingException.class),
    ALREADY_EXIST_LIKE_VIDEO("이미 좋아요를 등록한 동영상입니다.", BAD_REQUEST, AlreadyExistLikeVideoException.class),
    NOT_EXIST_LIKE_VIDEO("존재하지 않는 좋아요입니다.", BAD_REQUEST, NotExistLikeVideoException.class),
    NOT_MEMBER_OF_LIKE_VIDEO("본인이 등록한 좋아요만 취소할 수 있습니다.", BAD_REQUEST, NotMemberOfLikeVideoException.class),
    NOT_AUTHOR_EXCEPTION("접근권한이 없습니다.",UNAUTHORIZED, NotAuthorException.class);

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
