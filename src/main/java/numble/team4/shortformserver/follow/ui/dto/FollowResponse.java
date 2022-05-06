package numble.team4.shortformserver.follow.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.member.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class FollowResponse {

    private Long id;
    private MemberDto member;

    public static List<FollowResponse> from(List<Follow> followList) {
        return followList.stream()
                .map(FollowResponse::from)
                .collect(Collectors.toList());
    }

    private static FollowResponse from(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                MemberDto.from(follow.getFromMember())
        );
    }

    @Data
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class MemberDto {
        private Long id;
        private String name;
        private String profileImageUrl;

        public static MemberDto from(Member member) {
            return new MemberDto(
                    member.getId(),
                    member.getName(),
                    member.getProfileImageUrl()
            );
        }
    }
}
