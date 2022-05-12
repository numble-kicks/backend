package numble.team4.shortformserver.member.member.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import numble.team4.shortformserver.member.member.domain.Member;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberInfoResponse {

    private Long id;
    private String email;
    private String name;
    private String profileImageUrl;
    private boolean emailVerified;
    private LocalDateTime createAt;
    private LocalDateTime lastLoginDate;

    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getProfileImageUrl(),
                member.isEmailVerified(),
                member.getCreateAt(),
                member.getLastLoginDate()
        );
    }

}
