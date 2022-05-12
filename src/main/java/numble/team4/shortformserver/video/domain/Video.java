package numble.team4.shortformserver.video.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.common.domain.BaseTimeEntity;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.exception.NotAuthorException;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Video extends BaseTimeEntity {

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

    private String hitsCursor;
    private String likesCursor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void update(String title, String description, Member member) {
        this.title = title;
        this.description = description;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
        this.hitsCursor = lpad(this.viewCount);
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
        this.likesCursor = lpad(this.likeCount);
    }

    public void decreaseLikeCount() {
        this.likeCount -= 1;
        this.likesCursor = lpad(this.likeCount);
    }

    public void validateAuthor(Member member) {
        if (!this.member.equals(member)) {
            throw new NotAuthorException();
        }
    }

    private String lpad(Long cursor) {
        return String.format("%05d", cursor) + String.format("%05d", this.id);
    }
}
