package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.follow.infrastructure.FollowCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);

}
