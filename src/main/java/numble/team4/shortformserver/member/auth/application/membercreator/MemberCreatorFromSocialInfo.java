package numble.team4.shortformserver.member.auth.application.membercreator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;

public interface MemberCreatorFromSocialInfo {
    Member signUpOrLoginMember(String response);
    OauthProvider getProviderName();
}
