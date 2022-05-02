package numble.team4.shortformserver.follow.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);

    void deleteByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);
}
