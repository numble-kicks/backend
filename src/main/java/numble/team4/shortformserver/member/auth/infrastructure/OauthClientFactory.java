package numble.team4.shortformserver.member.auth.infrastructure;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static numble.team4.shortformserver.member.auth.domain.OauthProvider.findMatchProvider;

@Component
public class OauthClientFactory {

    private final Map<OauthProvider, OauthClient> providers = new HashMap<>();

    @Autowired
    public OauthClientFactory(Set<OauthClient> clients) {
        registerClient(clients);
    }

    public OauthClient findOauthClient(String providerName) {
        return providers.get(findMatchProvider(providerName));
    }

    private void registerClient(Set<OauthClient> clients) {
        clients.forEach(client -> providers.put(client.getProviderName(), client));
    }
}
