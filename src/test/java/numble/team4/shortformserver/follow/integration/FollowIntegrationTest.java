package numble.team4.shortformserver.follow.integration;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.follow.ui.FollowController;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;

@BaseIntegrationTest
public class FollowIntegrationTest {

    @Autowired
    private FollowController followController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    private Member fromMember;
    private Member toMember;

    @BeforeEach
    void init() {
        fromMember = Member.builder().name("from").build();
        toMember = Member.builder().name("to").build();

        memberRepository.saveAll(Arrays.asList(fromMember, toMember));
    }

    @Nested
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 본인 외의 다른 사용자를 팔로우 한 상태에서 취소 요청")
        void deleteFollow_isok_success() {
            //given
            followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            followController.deleteFollow(fromMember.getId() , toMember.getId());

            //then
            Assertions.assertThat(followRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 사용자에 대해 팔로우 취소 요청")
        void deleteFollow_notExistMemberException_fail() {
            //when, then
            assertThrows(
                    NotExistMemberException.class,
                    () -> followController.deleteFollow(fromMember.getId(), 9999L)
            );
        }

        @Test
        @DisplayName("[실패] 팔로우하고 있지 않은 사용자를 취소 요청 (본인 팔로우 취소 요청을 포함)")
        void deleteFollow_notFollowingException_fail() {
            //when, then
            assertThrows(
                    NotFollowingException.class,
                    () -> followController.deleteFollow(fromMember.getId(), toMember.getId())
            );
        }
    }
}
