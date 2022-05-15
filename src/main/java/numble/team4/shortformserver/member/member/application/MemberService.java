package numble.team4.shortformserver.member.member.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.common.dto.CommonResponse;
import numble.team4.shortformserver.common.dto.PageInfo;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.member.member.application.dto.MemberInfoResponse;
import numble.team4.shortformserver.member.member.application.dto.MemberInfoResponseForAdmin;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.member.member.ui.dto.AllMemberInfoRequest;
import numble.team4.shortformserver.member.member.ui.dto.MemberEmailRequest;
import numble.team4.shortformserver.member.member.ui.dto.MemberNameUpdateRequest;
import numble.team4.shortformserver.video.domain.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static numble.team4.shortformserver.member.member.ui.MemberResponseMessage.GET_MEMBER_INFO;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final VideoRepository videoRepository;
    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3Uploader uploader;

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMemberException::new);
        long followers = followRepository.countByToMember(member);
        long followings = followRepository.countByFromMember(member);
        long videos = videoRepository.countByMember(member);

        return MemberInfoResponse.of(member, followers, followings, videos);
    }

    @Transactional
    public void saveProfileImage(Member member, MultipartFile file) {
        S3UploadDto uploadDto = uploader.saveToS3(file, "user");

        if (StringUtils.hasText(member.getProfileImageUrl())) {
            uploader.deleteToS3(member.getProfileImageUrl());
        }
        member.updateProfileImage(uploadDto.getFileUrl());
        memberRepository.save(member);
    }

    @Transactional
    public void updateUserName(Member member, MemberNameUpdateRequest request) {
        member.updateName(request.getName());
        memberRepository.save(member);
    }

    @Transactional
    public void updateUserEmail(Member member, MemberEmailRequest request) {
        member.updateEmail(request.getEmail());
        memberRepository.save(member);
    }

    public CommonResponse<List<MemberInfoResponseForAdmin>> getAllMemberInfo(AllMemberInfoRequest request, Pageable pageable) {
        Page<MemberInfoResponseForAdmin> members = memberRepository.findAllMembersByKeyword(request.getKeyword(), pageable)
                .map(MemberInfoResponseForAdmin::from);
        return CommonResponse.of(members.getContent(), PageInfo.from(members), GET_MEMBER_INFO.getMessage());
    }

    @Transactional
    public void deleteMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(NotExistMemberException::new);
        memberRepository.delete(member);
    }
}

