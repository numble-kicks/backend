package numble.team4.shortformserver.member.auth.ui;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.application.AuthService;
import numble.team4.shortformserver.member.auth.application.dto.LoginResponse;
import numble.team4.shortformserver.member.auth.ui.dto.OauthLoginRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static numble.team4.shortformserver.member.auth.ui.AuthResponseMessage.LOGIN_SUCCESS;
import static numble.team4.shortformserver.member.auth.ui.AuthResponseMessage.TOKEN_RENEW_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/oauth2/code/{registrationId}")
    public CommonResponse<LoginResponse> socialLogin(@Valid @RequestBody OauthLoginRequest request, @PathVariable String registrationId) {
        return CommonResponse.of(authService.signUpOrLogin(request.getCode(), registrationId), LOGIN_SUCCESS.getMessage());
    }

    @GetMapping("/renew")
    public CommonResponse<LoginResponse> oauthLogin(HttpServletRequest request) {
        return CommonResponse.of(authService.renewToken(request), TOKEN_RENEW_SUCCESS.getMessage());
    }
}
