package numble.team4.shortformserver.member.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.auth.util.dto.JwtTokenDto;
import numble.team4.shortformserver.member.member.domain.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String email;
    private String name;
    private JwtTokenDto token;

    public static LoginResponse of(Member member, JwtTokenDto token) {
        return new LoginResponse(member.getEmail(), member.getName(), token);
    }
}
