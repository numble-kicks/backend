package numble.team4.shortformserver.member.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import numble.team4.shortformserver.member.auth.application.membercreator.MemberCreatorFactory;
import numble.team4.shortformserver.member.auth.application.membercreator.MemberCreatorFromSocialInfo;
import numble.team4.shortformserver.member.auth.exception.JwtTokenExpiredException;
import numble.team4.shortformserver.member.auth.infrastructure.OauthClient;
import numble.team4.shortformserver.member.auth.infrastructure.OauthClientFactory;
import numble.team4.shortformserver.member.auth.util.JwtTokenProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final OauthClientFactory oauthProviderFactory;
    private final MemberCreatorFactory memberCreatorFactory;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String findProviderRedirectUrl(String provider) {
        OauthClient oauthClient = oauthProviderFactory.findOauthClient(provider);
        return oauthClient.getRedirectUrl();
    }

    public LoginResponse signUpOrLogin(String code, String provider) {
        OauthClient oauthClient = oauthProviderFactory.findOauthClient(provider);
        MemberCreatorFromSocialInfo creator = memberCreatorFactory.findMemberCreator(provider);

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
