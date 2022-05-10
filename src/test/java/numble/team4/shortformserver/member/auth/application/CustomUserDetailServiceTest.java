package numble.team4.shortformserver.member.auth.application;

import numble.team4.shortformserver.member.auth.exception.EmailEmptyException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @InjectMocks
    private CustomUserDetailService userDetailService;

    @Mock
    private MemberRepository memberRepository;

    @DisplayName("유저 이메일로 해당 유저가 존재하는지 조회한 후 있으면 기존 유저를, 없으면 새로운 유저를 등록해서 반환")
    @Test
    void loadUserByUsernameTest() {
        Member member = new Member("numble@numble.com", "numble", MEMBER, true);
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        UserDetails userDetails = userDetailService.loadUserByUsername(String.valueOf(1L));
        Assertions.assertThat(userDetails.getUsername()).isEqualTo("1");
    }

    @DisplayName("유저 이메일이 없으면 예외 발생")
    @Test
    void loadUserByUsernameTestFail() {
        Member member = new Member(null, "numble", MEMBER, false);
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));
        Assertions.assertThatThrownBy(() -> userDetailService.loadUserByUsername(String.valueOf(1L)))
                .isInstanceOf(EmailEmptyException.class);
    }
}