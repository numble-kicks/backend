package numble.team4.shortformserver.member.member.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.member.application.MemberService;
import numble.team4.shortformserver.member.member.ui.dto.MemberInfoResponse;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/{memberId}")
public class MemberController {

    private final MemberService memberService;
    private final VideoService videoService;

    @GetMapping
    public CommonResponse<MemberInfoResponse> findMemberInfo(@PathVariable Long memberId) {
        MemberInfoResponse res = memberService.getMemberInfo(memberId);
        return CommonResponse.of(res, GET_MEMBER_INFO.getMessage());
    }

    @GetMapping("/videos")
    public CommonResponse<List<VideoListResponse>> findAllVideosByMember(@PathVariable Long memberId,
                                                                         @RequestParam(value = "last_video_id", required = false) Long videoId) {
        List<VideoListResponse> videos = videoService.findAllVideosByMember(memberId, videoId);
        return CommonResponse.of(videos, GET_ALL_VIDEOS_OF_MEMBER.getMessage());
    }

    @GetMapping("/likes")
    public CommonResponse<List<VideoListResponse>> findAllLikeVideosByMember(@PathVariable Long memberId,
                                                                             @RequestParam(value = "last_video_id", required = false) Long videoId) {
        List<VideoListResponse> videos = videoService.findAllLikeVideosByMember(memberId, videoId);
        return CommonResponse.of(videos, GET_ALL_LIKE_VIDEOS_OF_MEMBER.getMessage());
    }


}
