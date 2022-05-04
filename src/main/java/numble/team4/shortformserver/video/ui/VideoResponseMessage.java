package numble.team4.shortformserver.video.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoResponseMessage {
    UPLOAD_VIDEO("영상 업로드 성공");

    private final String message;
}
