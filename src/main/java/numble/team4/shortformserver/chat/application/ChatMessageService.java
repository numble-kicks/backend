package numble.team4.shortformserver.chat.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.chat.application.dto.ChatMessageResponse;
import numble.team4.shortformserver.chat.domain.message.ChatMessage;
import numble.team4.shortformserver.chat.domain.message.ChatMessageRepository;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import numble.team4.shortformserver.chat.domain.room.ChatRoomRepository;
import numble.team4.shortformserver.chat.exception.NotExistChatRoomException;
import numble.team4.shortformserver.chat.ui.dto.ChatMessageRequest;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMessage(ChatMessageRequest request) {
        Member member = memberRepository.findById(request.getUserId())
                .orElseThrow(NotExistMemberException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(NotExistChatRoomException::new);

        chatMessageRepository.save(ChatMessage.of(member, chatRoom, request.getMessage()));
    }

    public List<ChatMessageResponse> findAllChatMessages(Member member, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(NotExistChatRoomException::new);
        chatRoom.validateAuthorization(member);

        return chatMessageRepository.findByChatRoomId(roomId).stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}
