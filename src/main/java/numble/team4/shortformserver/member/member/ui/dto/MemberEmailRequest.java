package numble.team4.shortformserver.member.member.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberEmailRequest {

    @NotBlank(message = "이메일은 빈 값이 허용되지 않습니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

}
