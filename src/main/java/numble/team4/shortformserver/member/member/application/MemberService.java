package numble.team4.shortformserver.member.member.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.aws.application.AmazonS3Uploader;
import numble.team4.shortformserver.aws.dto.S3UploadDto;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.member.member.ui.dto.MemberInfoResponse;
import numble.team4.shortformserver.member.member.ui.dto.MemberNameUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AmazonS3Uploader uploader;

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMemberException::new);

        return MemberInfoResponse.from(member);
    }

    @Transactional
    public void saveProfileImage(Member member, MultipartFile file) {
        S3UploadDto uploadDto = uploader.saveToS3(file, "user");

        if (StringUtils.hasText(member.getProfileImageUrl())) {
            uploader.deleteToS3(member.getProfileImageUrl());
        }
        member.updateProfileImage(uploadDto.getFileUrl());
    }

    @Transactional
    public void updateUserName(Member member, MemberNameUpdateRequest request) {
        member.updateName(request.getName());
        memberRepository.save(member);
    }

}

