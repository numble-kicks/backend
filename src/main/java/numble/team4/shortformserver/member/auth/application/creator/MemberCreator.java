package numble.team4.shortformserver.member.auth.application.creator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;

import java.util.Map;

public interface MemberCreator {
    Member createMemberFromAttributes(Map<String, Object> attribute);
    OauthProvider getProvider();
}
