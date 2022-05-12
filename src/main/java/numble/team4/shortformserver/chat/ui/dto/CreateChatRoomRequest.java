package numble.team4.shortformserver.chat.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateChatRoomRequest {

    @NotBlank(message = "판매자의 ID를 입력해주세요.")
    private Long sellerId;
}
