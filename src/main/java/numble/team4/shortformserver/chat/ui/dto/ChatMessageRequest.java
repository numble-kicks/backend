package numble.team4.shortformserver.chat.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    @NotNull(message = "유저 ID를 입력해주세요.")
    private Long userId;

    @NotBlank(message = "메시지를 입력해주세요.")
    private String message;
}
