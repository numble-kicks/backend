package numble.team4.shortformserver.member.auth.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import numble.team4.shortformserver.member.auth.application.creator.MemberCreator;
import numble.team4.shortformserver.member.auth.application.creator.MemberCreatorFactory;
import numble.team4.shortformserver.member.auth.exception.JwtTokenExpiredException;
import numble.team4.shortformserver.member.auth.infrastructure.OAuthClient;
import numble.team4.shortformserver.member.auth.infrastructure.OAuthClientFactory;
import numble.team4.shortformserver.member.auth.util.JwtTokenProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberCreatorFactory creatorFactory;
    private final OAuthClientFactory clientFactory;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse signUpOrLogin(String code, String provider) {
        OAuthClient oauthClient = clientFactory.findOauthClient(provider);
        MemberCreator creator = creatorFactory.findMemberCreator(provider);

        Member member = creator.signUpOrLoginMember(oauthClient.getUserProfile(code));
        return LoginResponse.of(member, jwtTokenProvider.createTokens(member));
    }

    public LoginResponse renewToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.extractToken(request);

        if (jwtTokenProvider.validateTokenRefreshToken(refreshToken)) {
            Member member = memberRepository.findById(jwtTokenProvider.getUserIdFromRefreshToken(refreshToken))
                    .orElseThrow(NotExistMemberException::new);
            return LoginResponse.of(member, jwtTokenProvider.createTokens(member));
        }
        throw new JwtTokenExpiredException();
    }
}
