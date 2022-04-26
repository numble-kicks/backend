package numble.team4.shortformserver.video.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Video {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private Long viewCount;
    private Long likeCount;

    @Embedded
    private Comments comments;

    public void addCommentToVideo(Comment comment) {
        comments.addComment(comment);
    }
}