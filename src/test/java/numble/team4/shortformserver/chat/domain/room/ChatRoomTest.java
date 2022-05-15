package numble.team4.shortformserver.chat.domain.room;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.exception.NoAccessPermissionException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ChatRoomTest {

    @Test
    void validateAuthorizationTest() {
        Member buyer = Member.builder()
                .id(1L)
                .build();

        Member seller = Member.builder()
                .id(2L)
                .build();

        Member tester = Member.builder()
                .id(3L)
                .build();

        ChatRoom chatRoom = ChatRoom.of(buyer, seller);
        assertThatThrownBy(() -> chatRoom.validateAuthorization(tester))
                        .isInstanceOf(NoAccessPermissionException.class);
    }
}