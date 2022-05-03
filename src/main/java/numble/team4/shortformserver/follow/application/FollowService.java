package numble.team4.shortformserver.follow.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createFollow(Member member, Long toMemberId) {
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(NotExistMemberException::new);

        if (followRepository.existsByFromMember_IdAndToMember_Id(member.getId(), toMemberId)) {
            throw new AlreadyExistFollowException();
        }

        Follow newFollow = Follow.fromMembers(member, toMember);
        followRepository.save(newFollow);
    }

    @Transactional
    public void deleteFollow(Member member, Long toMemberId) {
        memberRepository.findById(toMemberId).orElseThrow(NotExistMemberException::new);

        if (!followRepository.existsByFromMember_IdAndToMember_Id(member.getId(), toMemberId)) {
            throw new NotFollowingException();
        }
        followRepository.deleteByFromMember_IdAndToMember_Id(member.getId(), toMemberId);
    }

}
