package numble.team4.shortformserver.member.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import numble.team4.shortformserver.member.auth.domain.CustomOAuth2User;
import numble.team4.shortformserver.member.auth.util.JwtTokenProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.fasterxml.jackson.core.json.JsonWriteFeature.ESCAPE_NON_ASCII;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final String APPLICATION_JSON = "application/json; charset:UTF-8";
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        createLoginResponse(response, oAuth2User.getMember());
    }

    public void createLoginResponse(HttpServletResponse response, Member member) {
        LoginResponse loginResponse = LoginResponse.of(member, jwtTokenProvider.createTokens(member));

        response.setStatus(OK.value());
        response.setContentType(APPLICATION_JSON);
        objectMapper.getFactory().configure(ESCAPE_NON_ASCII.mappedFeature(), true);

        try{
            response.getWriter().write(objectMapper.writeValueAsString(loginResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
