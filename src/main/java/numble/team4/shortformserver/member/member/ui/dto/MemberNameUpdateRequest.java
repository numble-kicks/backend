package numble.team4.shortformserver.member.member.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class MemberNameUpdateRequest {

    @NotBlank(message = "변경하실 닉네임을 입력해주세요.")
    private String name;
}
