package numble.team4.shortformserver.member.auth.infrastructure;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;

public interface OAuthClient {
    String getUserProfile(String code);
    OauthProvider getProviderName();
}
