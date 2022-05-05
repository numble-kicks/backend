package numble.team4.shortformserver.member.auth.infrastructure;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OauthClientFactoryTest {

    private OauthClientFactory factory = new OauthClientFactory(new HashSet<>(List.of(new TestClient())));

    @Test
    void findClientTest() {
        OauthClient testClient = factory.findOauthClient("kakao");
        assertThat(testClient.getProviderName()).isEqualTo(OauthProvider.KAKAO);
    }

    static class TestClient implements OauthClient {

        @Override
        public String getRedirectUrl() {
            return null;
        }

        @Override
        public String getUserProfile(String code) {
            return null;
        }

        @Override
        public OauthProvider getProviderName() {
            return OauthProvider.KAKAO;
        }
    }
}