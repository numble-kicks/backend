package numble.team4.shortformserver.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import numble.team4.shortformserver.member.member.domain.Member;

@Getter
@AllArgsConstructor
public class MemberEmbeddedDto {
    private Long id;

    private String name;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    public static MemberEmbeddedDto from(Member member) {
        return new MemberEmbeddedDto(member.getId(), member.getName(), member.getProfileImageUrl());
    }
}
