package numble.team4.shortformserver.member.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OauthProvider {
    KAKAO("kakao");

    private final String providerName;
}
