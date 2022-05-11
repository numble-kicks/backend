package numble.team4.shortformserver.member.member.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.GET_ALL_VIDEOS_OF_MEMBER;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final VideoService videoService;

    @GetMapping("/v1/users/{memberId}/videos")
    public CommonResponse<List<VideoListResponse>> findAllVideosByMember(@PathVariable Long memberId,
                                                                     @RequestParam(value = "last_video_id", required = false) Long videoId) {
        List<VideoListResponse> videos = videoService.findAllVideosByMember(memberId, videoId);
        return CommonResponse.of(videos, GET_ALL_VIDEOS_OF_MEMBER.getMessage());
    }

}
