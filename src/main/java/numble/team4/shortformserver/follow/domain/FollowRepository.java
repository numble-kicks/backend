package numble.team4.shortformserver.follow.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
<<<<<<< HEAD

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);
=======
>>>>>>> 8ce1bb9 (feat: follow 도메인 생성)
}
