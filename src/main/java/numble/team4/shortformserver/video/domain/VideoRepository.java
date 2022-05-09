package numble.team4.shortformserver.video.domain;

import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("select v from Video v order by v.likeCount desc")
    Page<Video> findAllSortByLikeCount(Pageable pageable);

    @Query("select v from Video v where v.id < :videoId order by v.likeCount desc")
    Page<Video> findAllSortByLikeCount(@Param("videoId") Long videoId, Pageable pageable);

    @Query("select v from Video v order by v.viewCount desc")
    Page<Video> findAllSortByViewCount(Pageable pageable);

    @Query("select v from Video v where v.id < :videoId order by v.viewCount desc")
    Page<Video> findAllSortByViewCount(@Param("videoId") Long videoId, Pageable pageable);

    @Query("select v from Video v where v.member = :member order by v.createAt desc")
    Page<Video> findAllVideosOfMember(@Param("member") Member member, Pageable pageable);

    @Query("select v from Video v where v.member = :member and v.id < :videoId order by v.createAt desc ")
    Page<Video> findAllVideosOfMember(@Param("videoId") Long videoId, @Param("member") Member member, Pageable pageable);

    boolean existsById(Long id);
}
