package numble.team4.shortformserver.chat.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.chat.application.dto.ChatRoomResponse;
import numble.team4.shortformserver.chat.domain.room.ChatRoom;
import numble.team4.shortformserver.chat.domain.room.ChatRoomRepository;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.common.dto.PageInfo;
import numble.team4.shortformserver.event.domain.FcmEventDomain;
import numble.team4.shortformserver.event.util.FcmEventPublisher;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static numble.team4.shortformserver.chat.ui.ChatRoomResponseMessage.FIND_CHAT_ROOM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final FcmEventPublisher fcmEventPublisher;

    @Transactional
    public void createChatRoom(Member buyer, Long sellerId) {
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(NotExistMemberException::new);
        chatRoomRepository.findExactlyMatchRoom(buyer, seller)
                        .orElseGet(() -> {
                            ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(buyer, seller));
                            fcmEventPublisher.publishFcmTokenPushingEvent(buyer.getId(), sellerId, FcmEventDomain.CHATTING);
                            return chatRoom;
                        });
    }

    public CommonResponse<List<ChatRoomResponse>> findChatRooms(Member member, Pageable pageable) {
        Page<ChatRoomResponse> chatRooms = chatRoomRepository.findMyRooms(member, pageable)
                .map(ChatRoomResponse::from);
        return CommonResponse.of(chatRooms.getContent(), PageInfo.from(chatRooms), FIND_CHAT_ROOM.getMessage());
    }
}
