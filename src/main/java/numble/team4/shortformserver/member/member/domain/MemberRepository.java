package numble.team4.shortformserver.member.member.domain;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUserIdAndProvider(Long userId, OauthProvider provider);
}
