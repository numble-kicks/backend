package numble.team4.shortformserver.likevideo.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LikeVideoResponseMessage {

    SAVE_LIKE_VIDEO("동영상에 좋아요 추가 성공");

    private final String message;
}
