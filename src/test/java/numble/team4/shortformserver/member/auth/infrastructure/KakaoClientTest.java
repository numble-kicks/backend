package numble.team4.shortformserver.member.auth.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KakaoClientTest {

    @Value("${social.kakao.redirectUrl}")
    private String redirectUrl;

    @InjectMocks
    private KakaoClient kakaoClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;


    @DisplayName("소셜로그인 제공사 이름 조회 테스트")
    @Test
    void getProviderNameTest() {
        assertThat(kakaoClient.getProviderName()).isEqualTo(OauthProvider.KAKAO);
    }

    @DisplayName("카카오 리다이렉트 URL 조회 테스트")
    @Test
    void getRedirectUrlTest() {
        assertThat(kakaoClient.getRedirectUrl()).isEqualTo(redirectUrl);
    }
}