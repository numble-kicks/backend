package numble.team4.shortformserver.likevideo.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeVideoResponseMessage {

    GET_IS_EXIST_LIKE_VIDEO("동영상 좋아요 등록 여부 조회 성공"),
    SAVE_LIKE_VIDEO("동영상에 좋아요 등록 성공"),
    DELETE_LIKE_VIDEO("동영상에 등록한 좋아요 삭제 성공");

    private final String message;
}
