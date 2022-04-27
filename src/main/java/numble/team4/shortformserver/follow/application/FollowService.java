package numble.team4.shortformserver.follow.application;

import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
<<<<<<< HEAD
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
=======
>>>>>>> 8cf2c71 (feat: 요청된 팔로우 생성 service 추가)
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

<<<<<<< HEAD
        Follow newFollow = Follow.fromMembers(member, toMember);
        followRepository.save(newFollow);
    }

=======
        Follow newFollow = Follow.builder()
                .fromMember(member).toMember(toMember).build();
        followRepository.save(newFollow);
    }
>>>>>>> 8cf2c71 (feat: 요청된 팔로우 생성 service 추가)
}
