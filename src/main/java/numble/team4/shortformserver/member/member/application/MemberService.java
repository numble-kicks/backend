package numble.team4.shortformserver.member.member.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.member.member.ui.dto.MemberInfoResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotExistMemberException::new);

        return MemberInfoResponse.from(member);
    }

}

