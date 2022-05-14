package numble.team4.shortformserver.member.auth.application.creator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;

public interface MemberCreator {
    Member signUpOrLoginMember(String response);
    OauthProvider getProvider();
}
