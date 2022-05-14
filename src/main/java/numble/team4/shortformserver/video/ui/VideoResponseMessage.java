package numble.team4.shortformserver.video.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoResponseMessage {
    UPLOAD_VIDEO("영상 업로드 성공"),
    UPDATE_VIDEO("영상 수정 성공"),
    DELETE_VIDEO("영상 삭제 성공"),
    GET_ALL_VIDEO("전체 영상 조회 성공"),
    GET_VIDEO_BY_ID("영상 정보 조회 성공"),
    GET_VIDEO_LIST_BY_KEYWORD("영상 검색 성공"),
    GET_VIDEO_TOP_10("영상 top10 조회 성공"),
    GET_ADMIN_PAGE_VIDEO_LIST("관리자 페이지 영상 목록 가져오기 성공");


    private final String message;
}
