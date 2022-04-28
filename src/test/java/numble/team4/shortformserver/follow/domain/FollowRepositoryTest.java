package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private MemberRepository memberRepository;

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
    }

    @Nested
    @DisplayName("팔로우 저장 테스트")
    class saveFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 상태의 팔로우")
        void save_createFollow_success() {
            //given
            Member from = makeTestFromUser();
            Member to = makeTestToUser();

            Follow follow = Follow.fromMembers(from, to);

            //when
            followRepository.save(follow);

            //then
            Assertions.assertThat(followRepository.findAll()).hasSize(1);
        }

    }

}