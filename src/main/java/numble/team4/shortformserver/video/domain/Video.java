package numble.team4.shortformserver.video.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Video {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private String videoUrl;

    private Long viewCount;
    private Long likeCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Comments comments;

    @Builder
    public Video(String title, String description, String thumbnailUrl, String videoUrl,
        Long viewCount, Long likeCount,
        Member member) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.member = member;

        if (Objects.nonNull(member)) {
            setMember(member);
        }
    }

    public void setMember(Member member) {
        this.member = member;
        member.getVideos().add(this);
    }


    public void addCommentToVideo(Comment comment) {
        comments.addComment(comment);
    }
}
