package numble.team4.shortformserver.member.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("관리자"),
    ROLE_MEMBER("회원");

    private final String description;
}
