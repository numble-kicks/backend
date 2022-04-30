package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

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
                .emailVerified(true)
                .build();
        toUser = Member.builder()
                .emailVerified(true)
                .build();
        memberRepository.save(fromUser);
        memberRepository.save(toUser);
    }

    @Nested
    @DisplayName("팔로우 저장 테스트")
    class saveFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 상태의 팔로우")
        void save_createFollow_success() {
            //given
            Follow follow = Follow.fromMembers(fromUser, toUser);

            //when
            followRepository.save(follow);

            //then
            Assertions.assertThat(followRepository.findAll()).hasSize(1);
        }

    }

}