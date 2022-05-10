package numble.team4.shortformserver.member.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자", "ROLE_ADMIN"),
    MEMBER("회원", "ROLE_MEMBER");

    private final String description;
    private final String securityRole;
}
