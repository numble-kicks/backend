package numble.team4.shortformserver.member.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.Role;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class MemberInfoResponseForAdmin {
    private Long id;
    private Long userId;
    private String email;
    private String name;
    private Role role;
    private OauthProvider provider;
    private LocalDateTime lastLoginDate;
    private String profileImageUrl;
    private boolean emailVerified;

    public static MemberInfoResponseForAdmin from(Member member) {
        return new MemberInfoResponseForAdmin(
                member.getId(),
                member.getUserId(),
                member.getEmail(),
                member.getName(),
                member.getRole(),
                member.getProvider(),
                member.getLastLoginDate(),
                member.getProfileImageUrl(),
                member.isEmailVerified()
        );
    }
}
