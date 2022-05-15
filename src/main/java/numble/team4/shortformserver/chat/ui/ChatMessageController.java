package numble.team4.shortformserver.chat.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.chat.application.ChatMessageService;
import numble.team4.shortformserver.chat.application.dto.ChatMessageResponse;
import numble.team4.shortformserver.chat.ui.dto.ChatMessageRequest;
import numble.team4.shortformserver.chat.ui.dto.FindChatMessageRequest;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static numble.team4.shortformserver.chat.ui.ChatRoomResponseMessage.FIND_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/rooms/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @Valid @RequestBody ChatMessageRequest request) {
        chatMessageService.saveMessage(roomId, request);
        simpMessagingTemplate.convertAndSend("/sub/rooms/" + roomId, request.getMessage());
    }

    @GetMapping("/rooms/{roomId}/messages")
    public CommonResponse<List<ChatMessageResponse>> findAllChats(
            @LoginUser Member member,
            @PathVariable Long roomId,
            @Valid FindChatMessageRequest request) {
        return CommonResponse.of(chatMessageService.findAllChatMessages(member, roomId, request), FIND_MESSAGE.getMessage());
    }
}
