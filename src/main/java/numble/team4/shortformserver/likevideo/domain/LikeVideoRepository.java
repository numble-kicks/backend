package numble.team4.shortformserver.likevideo.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeVideoRepository extends JpaRepository<LikeVideo, Long> {

    boolean existsByMember_IdAndVideo_Id(Long MemberId, Long VideoId);

    @Query("select m.id from LikeVideo m where m.member=:member and m.video.id=:videoId")
    Optional<Long> findIdByMemberAndVideoId(@Param("member") Member member, @Param("videoId") Long videoId);

}
