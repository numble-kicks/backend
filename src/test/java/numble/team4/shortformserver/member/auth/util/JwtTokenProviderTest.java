package numble.team4.shortformserver.member.auth.util;

import numble.team4.shortformserver.member.auth.application.CustomUserDetailService;
import numble.team4.shortformserver.member.auth.util.dto.JwtTokenDto;
import numble.team4.shortformserver.member.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Set;

import static numble.team4.shortformserver.member.member.domain.Role.ROLE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailService customUserDetailService;

    private Member member;

    @BeforeEach
    void init() {
        member = new Member("numble@numble.com", "numble", ROLE_MEMBER, true);
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "test");
        ReflectionTestUtils.setField(jwtTokenProvider, "REFRESH_KEY", "test123");
    }

    @DisplayName("토큰 생성 및 토큰으로부터 유저 아이디 추출 테스트")
    @Test
    void createTokenAndGetUserIdTest() {
        JwtTokenDto tokens = jwtTokenProvider.createTokens(member);
        assertThat(jwtTokenProvider.getUserIdFromAccessToken(tokens.getAccessToken())).isEqualTo(1L);
        assertThat(jwtTokenProvider.getUserIdFromRefreshToken(tokens.getRefreshToken())).isEqualTo(1L);
    }

    @DisplayName("토큰 생성 및 토큰 유효성 검증 테스트")
    @Test
    void validateTokenTest() {
        JwtTokenDto tokens = jwtTokenProvider.createTokens(member);
        assertThat(jwtTokenProvider.validateTokenSecretToken(tokens.getAccessToken())).isTrue();
        assertThat(jwtTokenProvider.validateTokenRefreshToken(tokens.getRefreshToken())).isTrue();
    }

    @DisplayName("request로부터 토큰 추출 테스트")
    @Test
    void extractTokenTest() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjUxMzk1NzY1LCJleHAiOjE2NTEzOTc1NjV9.3ZYKd57zfInTCs2B_bVdEg5SOBHQ6Hjbhq8PIbaimxg";
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        request.addHeader("Authorization", String.format("Bearer %s", token));
        assertThat(jwtTokenProvider.extractToken(request)).isEqualTo(token);
    }

    @DisplayName("토큰으로부터 유저의 권한을 추출하는 기능 테스트")
    @Test
    void getAuthenticationTest() {
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("회원"));
        given(customUserDetailService.loadUserByUsername("1"))
                .willReturn(new User("numble@numble.com", "", authorities));
        String accessToken = jwtTokenProvider.createTokens(member).getAccessToken();
        User user = (User) jwtTokenProvider.getAuthentication(accessToken).getPrincipal();
        assertThat(user.getUsername()).isEqualTo("numble@numble.com");
    }
}