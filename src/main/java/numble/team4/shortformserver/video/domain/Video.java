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
import numble.team4.shortformserver.video.category.domain.Category;
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

    @Column(nullable = false)
    private Boolean usedStatus;

    @Column(nullable = false)
    private Long price;

    private Long viewCount;
    private Long likeCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void addToMember(Member member) {
        this.member = member;
        member.saveNewVideo(this);
    }

    public void update(String title, String description, Member member) {
        validateAuthor(member);

        this.title = title;
        this.description = description;
    }

    public void delete(Member member) {
        validateAuthor(member);
        member.removeVideo(this);
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    private void validateAuthor(Member member) {
        if (!this.member.equals(member)) {
            throw new NotAuthorException();
        }
    }
}
