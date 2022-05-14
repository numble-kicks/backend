package numble.team4.shortformserver.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.chat.domain.message.ChatMessage;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {

    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getMember().getId(),
                chatMessage.getMember().getName(),
                chatMessage.getMessage(),
                chatMessage.getCreateAt()
        );
    }
}
