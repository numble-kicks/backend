package numble.team4.shortformserver.video.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.infrastructure.VideoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoCustomRepository {
  
    long countByMember(Member member);
    boolean existsById(Long id);

}
