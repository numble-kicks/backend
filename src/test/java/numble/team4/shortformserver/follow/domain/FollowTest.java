package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.member.member.domain.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FollowTest {

    private static Member fromUser;

    @BeforeAll
    static void init() {
        fromUser = Member.builder()
                        .id(10L).emailVerified(true).build();
    }

    @Test
    @DisplayName("[실패] 본인을 팔로우")
    void fromMembers_notSelfFollowableException_fail() {
        //when, then
        assertThrows(NotSelfFollowableException.class,
                () -> Follow.fromMembers(fromUser, fromUser));
    }

}