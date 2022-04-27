package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
<<<<<<< HEAD
import org.junit.jupiter.api.*;
=======
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
>>>>>>> 9fd12e9 (test: follow 레포지토리 테스트 추가)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

<<<<<<< HEAD
=======
import java.util.List;

>>>>>>> 9fd12e9 (test: follow 레포지토리 테스트 추가)
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

<<<<<<< HEAD
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
=======
    Member makeTestToUser() {
        Member testUser1 = Member.builder()
                .emailVerified(true)
                .build();
        return memberRepository.save(testUser1);
    }

    Member makeTestFromUser() {
        Member testUser1 = Member.builder()
                .emailVerified(true)
                .build();
        return memberRepository.save(testUser1);
>>>>>>> 9fd12e9 (test: follow 레포지토리 테스트 추가)
    }

    @Nested
    @DisplayName("팔로우 저장 테스트")
    class saveFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 상태의 팔로우")
        void save_createFollow_success() {
            //given
<<<<<<< HEAD
            Follow follow = Follow.fromMembers(fromUser, toUser);
=======
            Member from = makeTestFromUser();
            Member to = makeTestToUser();
            Follow follow = Follow.builder()
                    .fromMember(from).toMember(to).build();
>>>>>>> 9fd12e9 (test: follow 레포지토리 테스트 추가)

            //when
            followRepository.save(follow);

            //then
            Assertions.assertThat(followRepository.findAll()).hasSize(1);
        }

    }

}