package numble.team4.shortformserver.member.auth.infrastructure;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;

public interface OauthClient {
    String getRedirectUrl();
    String getUserProfile(String code);
    OauthProvider getProviderName();
}
