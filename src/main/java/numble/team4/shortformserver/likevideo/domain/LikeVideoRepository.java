package numble.team4.shortformserver.likevideo.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeVideoRepository extends JpaRepository<LikeVideo, Long> {

    boolean existsByMember_IdAndVideo_Id(Long MemberId, Long VideoId);

}
