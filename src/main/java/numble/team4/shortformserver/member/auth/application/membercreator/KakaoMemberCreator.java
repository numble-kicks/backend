package numble.team4.shortformserver.member.auth.application.membercreator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.exception.JsonParsingException;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.stereotype.Component;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;

@Component
@RequiredArgsConstructor
public class KakaoMemberCreator implements MemberCreatorFromSocialInfo {

    private static final String PROPERTIES = "properties";
    private static final String NICKNAME = "nickname";
    private static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String EMAIL = "email";
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Member signUpOrLoginMember(String response) {
        String email = extractEmailFromResponse(response);
        String name = extractNameFromResponse(response);
        if (checkEmptyEmail(email))
            return memberRepository.save(new Member(email, name, MEMBER, false));
        return memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(new Member(email, name, MEMBER, true)));
    }

    @Override
    public OauthProvider getProviderName() {
        return OauthProvider.KAKAO;
    }

    private String extractNameFromResponse(String response) {
        try {
            return objectMapper.readTree(response)
                    .get(PROPERTIES)
                    .get(NICKNAME)
                    .asText();
        } catch (JsonProcessingException e) {
            throw new JsonParsingException();
        }
    }

    private String extractEmailFromResponse(String response) {
        try {
            JsonNode emailNode = objectMapper.readTree(response)
                    .get(KAKAO_ACCOUNT)
                    .get(EMAIL);
            if (emailNode != null) {
                return emailNode.asText();
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new JsonParsingException();
        }
    }

    private boolean checkEmptyEmail(String email) {
        return email == null;
    }
}
