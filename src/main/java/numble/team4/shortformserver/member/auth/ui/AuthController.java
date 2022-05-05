package numble.team4.shortformserver.member.auth.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.application.AuthService;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static numble.team4.shortformserver.member.auth.ui.AuthResponseMessage.LOGIN_SUCCESS;
import static numble.team4.shortformserver.member.auth.ui.AuthResponseMessage.TOKEN_RENEW_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/oauth/login")
    public void oauthLogin(String provider, HttpServletResponse response) throws IOException {
        response.sendRedirect(authService.findProviderRedirectUrl(provider));
    }

    @GetMapping("/oauth/callback")
    public CommonResponse<LoginResponse> callback(@RequestParam String code, @RequestParam String state) {
        return CommonResponse.of(authService.signUpOrLogin(code, state), LOGIN_SUCCESS.getMessage());
    }

    @GetMapping("/renew")
    public CommonResponse<LoginResponse> oauthLogin(HttpServletRequest request) {
        return CommonResponse.of(authService.renewToken(request), TOKEN_RENEW_SUCCESS.getMessage());
    }
}
