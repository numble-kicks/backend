package numble.team4.shortformserver.member.auth.infrastructure;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.auth.exception.NotExistProviderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    private OauthProvider findMatchProvider(String providerName) {
        return Arrays.stream(OauthProvider.values())
                .filter(provider -> provider.getProviderName().equals(providerName))
                .findAny()
                .orElseThrow(NotExistProviderException::new);
    }

    private void registerClient(Set<OauthClient> clients) {
        clients.forEach(client -> providers.put(client.getProviderName(), client));
    }
}
