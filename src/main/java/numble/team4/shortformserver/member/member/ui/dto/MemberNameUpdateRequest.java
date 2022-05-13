package numble.team4.shortformserver.member.member.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberNameUpdateRequest {

    @NotBlank(message = "변경하실 닉네임을 입력해주세요.")
    private String name;
}
