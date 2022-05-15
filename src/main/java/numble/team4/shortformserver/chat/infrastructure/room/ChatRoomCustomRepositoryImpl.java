package numble.team4.shortformserver.chat.infrastructure.room;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

import static numble.team4.shortformserver.chat.domain.room.QChatRoom.chatRoom;

@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Page<ChatRoom> findMyRooms(Member findMember, Pageable pageable) {
        List<ChatRoom> chatRooms = factory.selectFrom(chatRoom)
                .where(containsMember(findMember))
                .orderBy(chatRoom.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(chatRooms, pageable, chatRooms::size);
    }

    @Override
    public Optional<ChatRoom> findExactlyMatchRoom(Member buyer, Member seller) {
        return Optional.ofNullable(factory.selectFrom(chatRoom)
                .where(matchAllMember(buyer, seller))
                .fetchOne());
    }

    private BooleanExpression matchAllMember(Member buyer, Member seller) {
        return (chatRoom.buyer.eq(buyer).and(chatRoom.seller.eq(seller)))
                .or(chatRoom.buyer.eq(seller).and(chatRoom.seller.eq(buyer)));
    }

    private BooleanExpression containsMember(Member findMember) {
        return chatRoom.buyer.eq(findMember)
                .or(chatRoom.seller.eq(findMember));
    }
}
