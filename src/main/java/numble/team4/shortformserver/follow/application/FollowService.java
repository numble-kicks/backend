package numble.team4.shortformserver.follow.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public void createFollow(Member member, Long toMemberId) {
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(NotExistMemberException::new);

        if (followRepository.existsByFromMember_IdAndToMember_Id(member.getId(), toMemberId)) {
            throw new AlreadyExistFollowException();
        }

        Follow newFollow = Follow.builder()
                .fromMember(member).toMember(toMember).build();
        followRepository.save(newFollow);
    }
}
