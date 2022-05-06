package numble.team4.shortformserver.follow.integration;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.NotExistFollowException;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.follow.ui.FollowController;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
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
        fromMember = Member.builder().name("from").role(Role.ROLE_MEMBER).build();
        toMember = Member.builder().name("to").role(Role.ROLE_MEMBER).build();

        memberRepository.saveAll(Arrays.asList(fromMember, toMember));
    }

    @Nested
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 본인 외의 다른 사용자를 팔로우 한 상태에서 취소 요청")
        void deleteFollow_isok_success() {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            followController.deleteFollow(fromMember.getId() , follow.getId());

            //then
            Assertions.assertThat(followRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 팔로우에 대해 취소 요청")
        void deleteFollow_notExistMemberException_fail() {
            //when, then
            assertThrows(
                    NotExistFollowException.class,
                    () -> followController.deleteFollow(fromMember.getId(), 9999L)
            );
        }

        @Test
        @DisplayName("[실패] 다른 사람이 생성한 팔로우에 대해 취소 요청")
        void deleteFollow_notFollowingException_fail() {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when, then
            assertThrows(
                    NotFollowingException.class,
                    () -> followController.deleteFollow(toMember.getId(), follow.getId())
            );
        }
    }
}
