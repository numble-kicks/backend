package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BaseDataJpaTest
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager testEntityManager;

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

    @Test
    @DisplayName("[성공] 특정 사용자 팔로우 여부 확인 : 팔로우 중일 때")
    void findIdByFromMemberIdAndToMemberId_optionalHasValue_success() {
        //given
        Follow follow = Follow.fromMembers(fromUser, toUser);
        followRepository.save(follow);
        testEntityManager.flush();
        testEntityManager.clear();

        //when
        Optional<Long> existFollowInfo =
                followRepository.findIdByFromMemberIdAndToMemberId(fromUser, toUser.getId());

        //then
        assertThat(existFollowInfo.get()).isEqualTo(follow.getId());
    }


    @Test
    @DisplayName("[성공] 특정 사용자 팔로우 여부 확인 : 팔로우하지 않을 때")
    void findIdByFromMemberIdAndToMemberId_optionalIsEmpty_success() {
        //when
        Optional<Long> existFollowInfo =
                followRepository.findIdByFromMemberIdAndToMemberId(fromUser, 2390840923L);

        //then
        assertTrue(existFollowInfo.isEmpty());

    }

    @Test
    @DisplayName("[성공] 정상적인 상태의 팔로우 생성")
    void save_createFollow_success() {
        //given
        Follow follow = Follow.fromMembers(fromUser, toUser);

        //when
        followRepository.save(follow);

        //then
        assertThat(followRepository.findAll()).hasSize(1);
    }
}