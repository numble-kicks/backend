package numble.team4.shortformserver.member.auth.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.exception.KakaoLoginFailException;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoClient implements OauthClient {

    private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded;charset=utf-8";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String BEARER = "Bearer %s";
    private static final String CODE = "code";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.kakao.token_uri}")
    private String getAccessTokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String getUserProfileUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-url}")
    private String redirectUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public OauthProvider getProviderName() {
        return OauthProvider.KAKAO;
    }

    @Override
    public String getRedirectUrl() {
        return redirectUrl;
    }

    @Override
    public String getUserProfile(String code) {
        String accessToken = getAccessToken(code);
        HttpEntity<Object> httpEntity = createHttpEntityForUserProfile(accessToken);

        try {
            return restTemplate
                    .exchange(getUserProfileUrl, GET, httpEntity, String.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            throw new KakaoLoginFailException();
        }
    }

    private String getAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntityForGetAccessToken(code);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    getAccessTokenUrl,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            return getAccessTokenFromResponse(response);
        } catch (HttpClientErrorException | JsonProcessingException e) {
            throw new KakaoLoginFailException();
        }
    }

    private String getAccessTokenFromResponse(ResponseEntity<String> response) throws JsonProcessingException {
        return objectMapper.readTree(response.getBody())
                .get(ACCESS_TOKEN)
                .asText();
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntityForGetAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, X_WWW_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(GRANT_TYPE, AUTHORIZATION_CODE);
        params.add(CLIENT_ID, clientId);
        params.add(REDIRECT_URI, redirectUri);
        params.add(CODE, code);
        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<Object> createHttpEntityForUserProfile(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, String.format(BEARER, accessToken));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(httpHeaders);
    }
}
