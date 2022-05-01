package numble.team4.shortformserver.member.auth.application.membercreator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.auth.exception.NotExistProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private OauthProvider findMatchProvider(String providerName) {
        return Arrays.stream(OauthProvider.values())
                .filter(provider -> provider.isEqualProviderName(providerName))
                .findAny()
                .orElseThrow(NotExistProviderException::new);
    }

    private void registerCreators(Set<MemberCreatorFromSocialInfo> creators) {
        creators.forEach(creator -> providers.put(creator.getProviderName(), creator));
    }
}
