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

    private final Long id;
    private final String email;
    private final String name;
    private final String profileImageUrl;
    private final boolean emailVerified;
    private final LocalDateTime createAt;
    private final LocalDateTime lastLoginDate;
    private final long followers;
    private final long followings;
    private final long videos;

    public static MemberInfoResponse from(Member member, long followers, long followings, long videos) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getProfileImageUrl(),
                member.isEmailVerified(),
                member.getCreateAt(),
                member.getLastLoginDate(),
                followers,
                followings,
                videos
        );
    }

}
