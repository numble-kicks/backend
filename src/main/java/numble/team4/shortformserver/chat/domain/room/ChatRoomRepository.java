package numble.team4.shortformserver.chat.domain.room;

import numble.team4.shortformserver.chat.infrastructure.room.ChatRoomCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomCustomRepository {
}
