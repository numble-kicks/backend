package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
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
                .role(Role.ROLE_MEMBER)
                .emailVerified(true)
                .build();
        toUser = Member.builder()
                .role(Role.ROLE_MEMBER)
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

    @Nested
    @DisplayName("팔로우 조회 테스트")
    class GetFollowTest {

        @Test
        @DisplayName("[성공] 팔로잉 조회 테스트")
        void findByFromMember_Id_getFollowIsEqualFromMember_success() {
            //given
            Follow follow = Follow.fromMembers(fromUser, toUser);
            followRepository.save(follow);

            //when
            List<Follow> followings = followRepository.findByFromMember_Id(fromUser.getId());

            //then
            assertThat(followings).hasSize(1);
        }

        @Test
        @DisplayName("[성공] 팔로워 조회 테스트")
        void findByFromMember_Id_getFollowIsEqualToMember_success() {
            //given
            Follow follow = Follow.fromMembers(fromUser, toUser);
            followRepository.save(follow);

            //when
            List<Follow> followings = followRepository.findByToMember_Id(toUser.getId());

            //then
            assertThat(followings).hasSize(1);
        }


    }

}