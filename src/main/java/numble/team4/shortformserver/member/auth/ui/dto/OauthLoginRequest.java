package numble.team4.shortformserver.member.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginRequest {
    @NotBlank(message = "소셜로그인 제공사를 입력해주세요.")
    private String provider;
}
