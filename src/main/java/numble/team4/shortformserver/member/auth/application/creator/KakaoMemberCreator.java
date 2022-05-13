package numble.team4.shortformserver.member.auth.application.creator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.auth.exception.KakaoLoginFailException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

import static numble.team4.shortformserver.member.auth.domain.OauthProvider.KAKAO;
import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoMemberCreator implements MemberCreator {

    private static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String PROPERTIES = "properties";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email";
    private static final String ID = "id";

    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Member signUpOrLoginMember(String response) {
        Map<String, Object> attribute = extractAttributeFromResponse(response);
        Long userId = (Long) attribute.get("id");
        return memberRepository.findByUserIdAndProvider(userId, KAKAO)
                .orElseGet(() -> saveMember(attribute));
    }

    private Map<String, Object> extractAttributeFromResponse(String response) {
        try {
            return (Map<String, Object>) objectMapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            throw new KakaoLoginFailException();
        }
    }

    private Member saveMember(Map<String, Object> attribute) {
        String email = extractEmailFromAttribute(attribute);
        return memberRepository.save(new Member(
                (Long) attribute.get(ID),
                email,
                extractNameFromAttribute(attribute),
                MEMBER,
                KAKAO,
                StringUtils.hasText(email)
        ));
    }

    private String extractEmailFromAttribute(Map<String, Object> attribute) {
        try {
            return (String) ((Map<String, Object>) attribute.get(KAKAO_ACCOUNT))
                    .get(EMAIL);
        } catch (Exception e) {
            return null;
        }
    }

    private String extractNameFromAttribute(Map<String, Object> attribute) {
        return (String) ((Map<String, Object>) attribute.get(PROPERTIES)).get(NICKNAME);
    }

    @Override
    public OauthProvider getProviderName() {
        return KAKAO;
    }
}
