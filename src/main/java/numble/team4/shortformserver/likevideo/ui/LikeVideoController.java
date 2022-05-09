package numble.team4.shortformserver.likevideo.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.likevideo.application.LikeVideoService;
import numble.team4.shortformserver.likevideo.ui.dto.LikeVideoExistResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.web.bind.annotation.*;

import static numble.team4.shortformserver.likevideo.ui.LikeVideoResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/videos")
public class LikeVideoController {

    private final LikeVideoService likeVideoService;

    @GetMapping("/{videoId}/likes")
    public CommonResponse existLikeVideo(@LoginUser Member member, @PathVariable Long videoId) {
        LikeVideoExistResponse existLikeVideo = likeVideoService.existLikeVideo(member, videoId);
        return CommonResponse.of(existLikeVideo, GET_IS_EXIST_LIKE_VIDEO.getMessage());
    }

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
