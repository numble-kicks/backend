package numble.team4.shortformserver.likevideo.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.likevideo.application.LikeVideoService;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.web.bind.annotation.*;

import static numble.team4.shortformserver.likevideo.ui.LikeVideoResponseMessage.DELETE_LIKE_VIDEO;
import static numble.team4.shortformserver.likevideo.ui.LikeVideoResponseMessage.SAVE_LIKE_VIDEO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class LikeVideoController {

    private final LikeVideoService likeVideoService;

    @PostMapping("/{videoId}/likes")
    public CommonResponse saveLikeVideo(@LoginUser Member member, @PathVariable Long videoId) {
        likeVideoService.saveLikeVideo(member, videoId);
        return CommonResponse.from(SAVE_LIKE_VIDEO.getMessage());
    }

    @DeleteMapping("/likes/{likesId}")
    public CommonResponse deleteLikeVideo(@LoginUser Member member, @PathVariable Long likesId) {
        likeVideoService.deleteLikeVideo(member, likesId);
        return CommonResponse.from(DELETE_LIKE_VIDEO.getMessage());
    }
}
