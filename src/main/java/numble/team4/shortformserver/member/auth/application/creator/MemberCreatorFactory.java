package numble.team4.shortformserver.member.auth.application.creator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class MemberCreatorFactory {
    private final Map<OauthProvider, MemberCreator> creators = new HashMap<>();

    @Autowired
    public MemberCreatorFactory(Set<MemberCreator> creators) {
        registerCreators(creators);
    }

    public MemberCreator findMemberCreator(String providerName) {
        return creators.get(OauthProvider.findMatchProvider(providerName));
    }

    private void registerCreators(Set<MemberCreator> creators) {
        creators.forEach(creator -> this.creators.put(creator.getProvider(), creator));
    }
}
