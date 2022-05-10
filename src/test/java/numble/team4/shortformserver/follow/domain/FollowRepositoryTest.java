package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@BaseDataJpaTest
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static Member fromUser;
    private static Member toUser;

    @BeforeEach
    void init() {
        fromUser = Member.builder()
                .role(Role.MEMBER)
                .emailVerified(true)
                .build();
        toUser = Member.builder()
                .role(Role.MEMBER)
                .emailVerified(true)
                .build();
        memberRepository.save(fromUser);
        memberRepository.save(toUser);
    }

    @Nested
    @DisplayName("팔로우 저장 테스트")
    class SaveFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 상태의 팔로우")
        void save_createFollow_success() {
            //given
            Follow follow = Follow.fromMembers(fromUser, toUser);

            //when
            followRepository.save(follow);

            //then
            assertThat(followRepository.findAll()).hasSize(1);
        }

    }
}