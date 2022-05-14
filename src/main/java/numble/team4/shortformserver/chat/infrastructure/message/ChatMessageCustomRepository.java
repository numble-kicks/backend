package numble.team4.shortformserver.chat.infrastructure.message;

import numble.team4.shortformserver.chat.domain.message.ChatMessage;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageCustomRepository {
    List<ChatMessage> searchLastMessages(Long id, ChatRoom chatRoom, Pageable pageable);
}
