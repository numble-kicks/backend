package numble.team4.shortformserver.chat.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.chat.application.ChatRoomService;
import numble.team4.shortformserver.chat.application.dto.ChatRoomResponse;
import numble.team4.shortformserver.chat.ui.dto.CreateChatRoomRequest;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static numble.team4.shortformserver.chat.ui.ChatRoomResponseMessage.CREATE_CHAT_ROOM;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chat-rooms")
    public CommonResponse<Void> createChatRoom(@LoginUser Member member, @RequestBody CreateChatRoomRequest request) {
        chatRoomService.createChatRoom(member, request.getSellerId());
        return CommonResponse.from(CREATE_CHAT_ROOM.getMessage());
    }

    @GetMapping("/chat-rooms")
    public CommonResponse<List<ChatRoomResponse>> findChatRooms(@LoginUser Member member, Pageable pageable) {
        return chatRoomService.findChatRooms(member, pageable);
    }
}
