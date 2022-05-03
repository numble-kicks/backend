package numble.team4.shortformserver.member.member.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.video.domain.Video;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String email;
    private String name;
    private LocalDateTime lastLoginDate;
    private String profileImageUrl;
    private boolean emailVerified;

    @Builder.Default
    @OneToMany(mappedBy = "member")
    private List<Video> videos = new ArrayList<>();

    public boolean isEqualMember(Member member) {
        return id.equals(member.id);
    }
}