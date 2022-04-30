package numble.team4.shortformserver.member.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String email;
    private String name;
    private LocalDateTime lastLoginDate;
    private String profileImageUrl;
    private boolean emailVerified;

    public boolean isEqualMember(Member member) {
        return id.equals(member.id);
    }
}
