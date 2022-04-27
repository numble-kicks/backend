package numble.team4.shortformserver.follow.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
