package numble.team4.shortformserver.follow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.member.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "from_member", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_member", nullable = false)
    private Member toMember;

    public static Follow fromMembers(Member fromMember, Member toMember) {
        if (fromMember.equals(toMember)) {
            throw new NotSelfFollowableException();
        }
        return new Follow(null, fromMember, toMember);
    }

    public boolean isFollowing(Member fromMember) {
        return fromMember.equals(this.fromMember);
    }

}
