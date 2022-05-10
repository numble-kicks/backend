package numble.team4.shortformserver.member.auth.application.membercreator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static numble.team4.shortformserver.member.auth.domain.OauthProvider.findMatchProvider;

@Component
public class MemberCreatorFactory {

    private final Map<OauthProvider, MemberCreatorFromSocialInfo> providers = new HashMap<>();

    @Autowired
    public MemberCreatorFactory(Set<MemberCreatorFromSocialInfo> creators) {
        registerCreators(creators);
    }

    public MemberCreatorFromSocialInfo findMemberCreator(String providerName) {
        return providers.get(findMatchProvider(providerName));
    }

    private void registerCreators(Set<MemberCreatorFromSocialInfo> creators) {
        creators.forEach(creator -> providers.put(creator.getProviderName(), creator));
    }
}
