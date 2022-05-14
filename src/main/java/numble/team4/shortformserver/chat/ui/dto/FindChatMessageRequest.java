package numble.team4.shortformserver.chat.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class FindChatMessageRequest {

    @NotNull(message = "메시지 아이디를 입력해주세요.")
    private Long messageId;

    @NotNull(message = "ChatMessage 응답 크기를 입력해주세요.")
    private int size;
}
