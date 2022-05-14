package numble.team4.shortformserver.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private Long buyerId;
    private String buyerName;
    private Long sellerId;
    private String sellerName;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return new ChatRoomResponse(
                chatRoom.getId(),
                chatRoom.getBuyer().getId(),
                chatRoom.getBuyer().getName(),
                chatRoom.getSeller().getId(),
                chatRoom.getSeller().getName()
        );
    }
}
