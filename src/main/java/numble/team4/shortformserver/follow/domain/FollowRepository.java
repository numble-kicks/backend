package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.follow.infrastructure.FollowCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);

    List<Follow> findByFromMember_Id(Long id);

    List<Follow> findByToMember_Id(Long id);

}
