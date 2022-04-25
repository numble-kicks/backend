package numble.team4.shortformserver.video.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Video {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String videoUrl;
    private Long viewCount;

    @Embedded
    private Comments comments;

    public void addCommentToVideo(Comment comment) {
        comments.addComment(comment);
    }
}
