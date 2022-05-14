package numble.team4.shortformserver.chat.domain.message;

import numble.team4.shortformserver.chat.infrastructure.message.ChatMessageCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageCustomRepository {
}
