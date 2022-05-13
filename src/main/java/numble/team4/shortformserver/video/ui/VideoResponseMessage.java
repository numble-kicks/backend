package numble.team4.shortformserver.video.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoResponseMessage {
    UPLOAD_VIDEO("영상 업로드 성공"),
    UPDATE_VIDEO("영상 수정 성공"),
    DELETE_VIDEO("영상 삭제 성공"),
    GET_VIDEO_BY_ID("특정 영상 조회 성공"),
    GET_ALL_VIDEOS("영상 목록 조회 성공"),
    GET_ALL_VIDEOS_OF_MEMBER("회원의 영상 목록 조회 성공");


    private final String message;
}
