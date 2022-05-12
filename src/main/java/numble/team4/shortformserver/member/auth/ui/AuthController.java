package numble.team4.shortformserver.member.auth.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.application.AuthService;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static numble.team4.shortformserver.member.auth.ui.AuthResponseMessage.TOKEN_RENEW_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/renew")
    public CommonResponse<LoginResponse> oauthLogin(HttpServletRequest request) {
        return CommonResponse.of(authService.renewToken(request), TOKEN_RENEW_SUCCESS.getMessage());
    }
}
