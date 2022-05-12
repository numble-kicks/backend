package numble.team4.shortformserver.video.domain;

import numble.team4.shortformserver.video.infrastructure.VideoCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VideoRepository extends JpaRepository<Video, Long>, VideoCustomRepository {

    @Query("select v from Video v order by v.likeCount desc")
    Page<Video> findAllSortByLikeCount(Pageable pageable);

    @Query("select v from Video v where v.likeCount < :likes order by v.likeCount desc")
    Page<Video> findAllSortByLikeCount(@Param("likes") Long likes, Pageable pageable);

    @Query("select v from Video v order by v.viewCount desc")
    Page<Video> findAllSortByViewCount(Pageable pageable);

    @Query("select v from Video v where v.viewCount < :hits order by v.viewCount desc")
    Page<Video> findAllSortByViewCount(@Param("hits") Long hits, Pageable pageable);

    boolean existsById(Long id);
}
