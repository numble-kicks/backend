package numble.team4.shortformserver.member.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.video.domain.Video;

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
    private Long userId;
    private String email;
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OauthProvider provider;

    private LocalDateTime lastLoginDate;
    private String profileImageUrl;
    private boolean emailVerified;

    @Builder
    public Member(Long userId, String email, String name, Role role, OauthProvider provider, boolean emailVerified) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.emailVerified = emailVerified;
    }

    public boolean hasNotEmail() {
        return !emailVerified;
    }

    public void updateLastLoginDate() {
        lastLoginDate = LocalDateTime.now();
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
    private List<Video> videos = new ArrayList<>();
}

