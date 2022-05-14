package numble.team4.shortformserver.video.category.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryResponseMessage {
    GET_CATEGORIES("카테고리 목록 조회 성공");

    private final String message;
}
