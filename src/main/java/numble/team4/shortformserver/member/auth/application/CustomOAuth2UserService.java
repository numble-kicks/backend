package numble.team4.shortformserver.member.auth.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.application.creator.MemberCreator;
import numble.team4.shortformserver.member.auth.application.creator.MemberCreatorFactory;
import numble.team4.shortformserver.member.auth.domain.CustomOAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberCreatorFactory memberCreatorFactory;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attribute = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        MemberCreator memberCreator = memberCreatorFactory.findMemberCreator(registrationId);
        return new CustomOAuth2User(
                memberCreator.createMemberFromAttributes(attribute),
                oAuth2User
        );
    }
}
