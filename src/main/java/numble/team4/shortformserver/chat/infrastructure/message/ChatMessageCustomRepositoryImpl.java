package numble.team4.shortformserver.chat.infrastructure.message;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.chat.domain.message.ChatMessage;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

import static numble.team4.shortformserver.chat.domain.message.QChatMessage.chatMessage;

@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public List<ChatMessage> searchLastMessages(Long id, ChatRoom chatRoom, Pageable pageable) {
        return factory.selectFrom(chatMessage)
                .where(lessThan(id), chatMessage.chatRoom.eq(chatRoom))
                .orderBy(chatMessage.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression lessThan(Long id) {
        if (Objects.isNull(id)) {
            return chatMessage.id.lt(Long.MAX_VALUE);
        }
        return chatMessage.id.lt(id);
    }
}
