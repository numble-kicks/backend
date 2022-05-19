package numble.team4.shortformserver.follow.domain;

import numble.team4.shortformserver.follow.infrastructure.FollowCustomRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

    @Query("select f from Follow f where f.fromMember=:fromMember and f.toMember.id=:toMemberId")
    Optional<Long> findIdByFromMemberIdAndToMemberId(@Param("fromMember") Member fromMember, @Param("toMemberId") Long toMemberId);

    boolean existsByFromMember_IdAndToMember_Id(Long fromMember, Long toMember);

    long countByFromMember(Member member);

    long countByToMember(Member member);

}
