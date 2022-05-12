package numble.team4.shortformserver.chat.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    private Long roomId;

    private Long userId;

    private String message;
}
