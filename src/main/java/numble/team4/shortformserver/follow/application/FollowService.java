package numble.team4.shortformserver.follow.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotExistFollowException;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.follow.ui.dto.FollowResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public List<FollowResponse> getAllFollowings(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotExistMemberException();
        }
        return followRepository.getFollowingsByMemberId(memberId);
    }

    public List<FollowResponse> getAllFollowers(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotExistMemberException();
        }
        return followRepository.getFollowersByMemberId(memberId);
    }

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
    public void deleteFollow(Member member, Long followId) {
        Follow follow = followRepository.findById(followId)
                .orElseThrow(NotExistFollowException::new);

        if (!follow.isFollowing(member)) {
            throw new NotFollowingException();
        }
        followRepository.delete(follow);
    }

}
