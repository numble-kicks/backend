package numble.team4.shortformserver.likevideo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.video.domain.Video;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class LikeVideo {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    public static LikeVideo fromMemberAndVideo(Member member, Video video) {
        return new LikeVideo(null, member, video);
    }

    public boolean isMemberOf(Member member) {
        return this.member.equals(member);
    }
}
