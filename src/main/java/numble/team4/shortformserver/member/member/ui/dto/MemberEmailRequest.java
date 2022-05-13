package numble.team4.shortformserver.member.member.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class MemberEmailRequest {

    @NotBlank(message = "이메일은 빈 값이 허용되지 않습니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private final String email;

}