package numble.team4.shortformserver.follow.ui.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FollowResponse {

    private final Long id;
    private final MemberDto member;

    @QueryProjection
    public FollowResponse(Long id, MemberDto member) {
        this.id = id;
        this.member = member;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class MemberDto {
        private final Long id;
        private final String name;
        private final String profileImageUrl;

        @QueryProjection
        public MemberDto(Long id, String name, String profileImageUrl) {
            this.id = id;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }
    }
}
