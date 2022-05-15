package numble.team4.shortformserver.chat.domain.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.member.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String message;

    private ChatMessage(Member member, ChatRoom chatRoom, String message) {
        this.member = member;
        this.chatRoom = chatRoom;
        this.message = message;
    }

    public static ChatMessage of(Member member, ChatRoom chatRoom, String content) {
        return new ChatMessage(member, chatRoom, content);
    }
}
