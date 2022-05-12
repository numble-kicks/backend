package numble.team4.shortformserver.member.auth.application.creator;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

import static numble.team4.shortformserver.member.auth.domain.OauthProvider.KAKAO;
import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;

@Component
@Transactional
@RequiredArgsConstructor
public class KakaoMemberCreator implements MemberCreator {

    private final MemberRepository memberRepository;

    @Override
    public Member createMemberFromAttributes(Map<String, Object> attribute) {
        Long userId = (Long) attribute.get("id");
        return memberRepository.findByUserIdAndProvider(userId, KAKAO)
                .orElseGet(() -> saveMember(attribute));
    }

    @Override
    public OauthProvider getProvider() {
        return KAKAO;
    }

    private Member saveMember(Map<String, Object> attribute) {
        String email = extractEmailFromAttribute(attribute);
        return memberRepository.save(new Member(
                (Long) attribute.get("id"),
                email,
                extractNameFromAttribute(attribute),
                MEMBER,
                KAKAO,
                StringUtils.hasText(email)
        ));
    }

    private String extractEmailFromAttribute(Map<String, Object> attribute) {
        try {
            return (String) ((Map<String, Object>) attribute.get("kakao_account"))
                    .get("email");
        } catch (Exception e) {
            return null;
        }
    }

    private String extractNameFromAttribute(Map<String, Object> attribute) {
        return (String) ((Map<String, Object>) attribute.get("properties")).get("nickname");
    }
}
