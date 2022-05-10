package numble.team4.shortformserver.follow.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.follow.application.FollowService;
import numble.team4.shortformserver.follow.ui.dto.FollowResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static numble.team4.shortformserver.follow.ui.FollowResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/following")
public class FollowController {

    private final MemberRepository memberRepository;
    private final FollowService followService;

    @GetMapping("/from")
    public CommonResponse getAllFollowings(@RequestParam("from_member") Long memberId) {
        List<FollowResponse> followings = followService.getAllFollowings(memberId);
        return CommonResponse.of(followings, GET_FOLLOWINGS.getMessage());
    }

    @GetMapping("/to")
    public CommonResponse getAllFollowers(@RequestParam("to_member") Long memberId) {
        List<FollowResponse> followers = followService.getAllFollowers(memberId);
        return CommonResponse.of(followers, GET_FOLLOWERS.getMessage());
    }

    @GetMapping("/{toMemberId}")
    public CommonResponse createFollow(@LoginUser Member member, @PathVariable Long toMemberId) {
        followService.createFollow(member, toMemberId);
        return CommonResponse.from(CREATE_FOLLOW.getMessage());
    }

    @DeleteMapping("/{followId}")
    public CommonResponse deleteFollow(@LoginUser Member member, @PathVariable Long followId) {
        followService.deleteFollow(member, followId);
        return CommonResponse.from(DELETE_FOLLOW.getMessage());
    }
}
