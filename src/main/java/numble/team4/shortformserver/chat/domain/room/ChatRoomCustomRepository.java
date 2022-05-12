package numble.team4.shortformserver.chat.domain.room;

import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChatRoomCustomRepository {
    Page<ChatRoom> findChatRoomByMember(Member member, Pageable pageable);
    Optional<ChatRoom> findChatRoomByAllMember(Member buyer, Member seller);
}
