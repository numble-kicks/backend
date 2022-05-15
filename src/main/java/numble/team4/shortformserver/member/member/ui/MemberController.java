package numble.team4.shortformserver.member.member.ui;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.member.auth.util.LoginUser;
import numble.team4.shortformserver.member.member.application.MailService;
import numble.team4.shortformserver.member.member.application.dto.MemberInfoResponse;
import numble.team4.shortformserver.member.member.application.MemberService;
import numble.team4.shortformserver.member.member.application.dto.MemberInfoResponseForAdmin;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.ui.dto.*;
import numble.team4.shortformserver.video.application.VideoService;
import numble.team4.shortformserver.video.dto.VideoListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class MemberController {

    private final MemberService memberService;
    private final VideoService videoService;
    private final MailService mailService;

    @GetMapping
    public CommonResponse<List<MemberInfoResponseForAdmin>> findAllMember(AllMemberInfoRequest request, Pageable pageable) {
        return memberService.getAllMemberInfo(request, pageable);
    }

    @DeleteMapping("/{memberId}")
    public CommonResponse<Void> deleteMemberById(@PathVariable Long memberId) {
        memberService.deleteMemberById(memberId);
        return CommonResponse.from(DELETE_MEMBER_INFO.getMessage());
    }

    @GetMapping("/{memberId}")
    public CommonResponse<MemberInfoResponse> findMemberInfo(@PathVariable Long memberId) {
        MemberInfoResponse res = memberService.getMemberInfo(memberId);
        return CommonResponse.of(res, GET_MEMBER_INFO.getMessage());
    }

    @GetMapping("/{memberId}/videos")
    public CommonResponse<List<VideoListResponse>> findAllVideosByMember(@PathVariable Long memberId,
                                                                         @RequestParam(value = "last_video_id", required = false) Long videoId) {
        List<VideoListResponse> videos = videoService.findAllVideosByMember(memberId, videoId);
        return CommonResponse.of(videos, GET_ALL_VIDEOS_OF_MEMBER.getMessage());
    }

    @GetMapping("/{memberId}/likes")
    public CommonResponse<List<VideoListResponse>> findAllLikeVideosByMember(@PathVariable Long memberId,
                                                                             @RequestParam(value = "last_video_id", required = false) Long videoId) {
        List<VideoListResponse> videos = videoService.findAllLikeVideosByMember(memberId, videoId);
        return CommonResponse.of(videos, GET_ALL_LIKE_VIDEOS_OF_MEMBER.getMessage());
    }

    @PutMapping("/image")
    public CommonResponse<Void> updateProfileImage(@LoginUser Member member, MultipartFile file) {
        memberService.saveProfileImage(member, file);
        return CommonResponse.from(SAVE_PROFILE_IMAGE.getMessage());
    }

    @PutMapping("/name")
    public CommonResponse<Void> updateUserName(@LoginUser Member member, @RequestBody @Valid MemberNameUpdateRequest request) {
        memberService.updateUserName(member, request);
        return CommonResponse.from(UPDATE_USER_NAME.getMessage());
    }

    @PostMapping("/email")
    public CommonResponse<Void> saveEmail(@LoginUser Member member, @RequestBody @Valid MemberEmailRequest request) {
        memberService.updateUserEmail(member, request);
        return CommonResponse.from(SAVE_MEMBER_EMAIL.getMessage());
    }

    @PostMapping("/email/auth")
    public CommonResponse<EmailAuthResponse> getMailAuthNumber(@RequestBody @Valid MemberEmailRequest request) {
        EmailAuthResponse response = mailService.sendAuthMail(request);
        return CommonResponse.of(response, GET_MAIL_AUTH_NUMBER.getMessage());
    }
}
