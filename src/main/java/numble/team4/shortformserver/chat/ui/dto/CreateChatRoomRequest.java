package numble.team4.shortformserver.chat.ui.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateChatRoomRequest {

    @NotNull(message = "판매자의 ID를 입력해주세요.")
    private Long sellerId;
}
