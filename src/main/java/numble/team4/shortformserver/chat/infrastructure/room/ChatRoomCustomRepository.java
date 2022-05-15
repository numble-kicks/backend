package numble.team4.shortformserver.chat.infrastructure.room;

import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRoomCustomRepository {
    Page<ChatRoom> findMyRooms(Member member, Pageable pageable);
    Optional<ChatRoom> findExactlyMatchRoom(Member buyer, Member seller);
}
