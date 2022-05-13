package numble.team4.shortformserver.member.member.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberNameUpdateRequest {

    @Length(min = 3, max = 10, message = "닉네임은 3~10자리로 입력해주세요.")
    @Pattern(regexp = "^[A-Za-zㄱ-ㅎ가-힣0-9]*$", message = "닉네임에는 숫자, 영어, 한글만 사용할 수 있습니다.")
    @NotBlank(message = "변경하실 닉네임을 입력해주세요.")
    private String name;
}
