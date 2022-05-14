package numble.team4.shortformserver.member.auth.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final Member member;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String name;

    public CustomOAuth2User(Member member, OAuth2User oAuth2User) {
        this.member = member;
        this.attributes = oAuth2User.getAttributes();
        this.authorities = oAuth2User.getAuthorities();
        this.name = oAuth2User.getName();
    }

    public Member getMember() {
        return member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }
}
