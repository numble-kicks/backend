package numble.team4.shortformserver.chat.ui;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoomResponseMessage {
    FIND_CHAT_ROOM("채팅방 조회 성공"),
    FIND_MESSAGE("메시지 조회 성공"),
    CREATE_CHAT_ROOM("채팅방 생성 성공");

    private final String message;
}
