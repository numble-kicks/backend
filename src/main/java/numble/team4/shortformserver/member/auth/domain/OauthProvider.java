package numble.team4.shortformserver.member.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.auth.exception.NotExistProviderException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OauthProvider {
    KAKAO("kakao");

    private final String providerName;

    public boolean isEqualProviderName(String providerName) {
        return this.providerName.equals(providerName);
    }

    public static OauthProvider findMatchProvider(String providerName) {
        return Arrays.stream(OauthProvider.values())
                .takeWhile(provider -> provider.isEqualProviderName(providerName))
                .findAny()
                .orElseThrow(NotExistProviderException::new);
    }
}
