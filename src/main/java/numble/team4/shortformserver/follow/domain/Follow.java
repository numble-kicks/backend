package numble.team4.shortformserver.follow.domain;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
=======
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> 8ce1bb9 (feat: follow 도메인 생성)
import numble.team4.shortformserver.member.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
<<<<<<< HEAD
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
=======

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
>>>>>>> 8ce1bb9 (feat: follow 도메인 생성)
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
<<<<<<< HEAD

    public static Follow fromMembers(Member fromMember, Member toMember) {
        if (fromMember.isEqualMember(toMember)) {
            throw new NotSelfFollowableException();
        }
        return new Follow(null, fromMember, toMember);
    }
=======
>>>>>>> 8ce1bb9 (feat: follow 도메인 생성)
}
