package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.follow.infrastructure.FollowCustomRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);

    long countByFromMember(Member member);

    long countByToMember(Member member);

}
