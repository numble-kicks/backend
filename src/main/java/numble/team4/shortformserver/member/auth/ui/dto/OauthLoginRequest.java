package numble.team4.shortformserver.member.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginRequest {
    @NotBlank(message = "소셜로그인 제공사로 부터 받은 code 입력해주세요.")
    private String code;
}
