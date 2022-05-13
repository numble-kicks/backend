package numble.team4.shortformserver.member.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.video.domain.Video;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass() && Hibernate.getClass(o) != getClass())) return false;
        Member member = (Member) o;
        return Objects.equals(getId(), member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @OneToMany(mappedBy = "member")
    private List<Video> videos = new ArrayList<>();
}

