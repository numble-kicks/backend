package numble.team4.shortformserver.member.auth.domain;

import lombok.Getter;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.UUID;

@Getter
public class MemberAdapter extends User {

    private final Member member;

    public MemberAdapter(Member member) {
        super(String.valueOf(member.getId()), UUID.randomUUID().toString(), Collections.singleton(new SimpleGrantedAuthority(member.getRole().getDescription())));
        this.member = member;
    }
}
