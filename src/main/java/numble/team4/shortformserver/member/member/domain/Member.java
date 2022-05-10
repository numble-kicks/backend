package numble.team4.shortformserver.member.member.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime lastLoginDate;
    private String profileImageUrl;
    private boolean emailVerified;

    public Member(String email, String name, Role role, boolean emailVerified) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.emailVerified = emailVerified;
    }

    public boolean hasNotEmail() {
        return !emailVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Video> videos = new ArrayList<>();

    public boolean isEqualMember(Member member) {
        return id.equals(member.id);
    }


    public void removeVideo(Video video) {
        this.videos.remove(video);
    }
}