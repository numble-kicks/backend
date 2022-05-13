package numble.team4.shortformserver.follow.infrastructure;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.ui.dto.FollowResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@BaseDataJpaTest
class FollowCustomRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(FollowCustomRepositoryTest.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    private Member fromUser;
    private Member toUser;
    private Member testUser;

    @BeforeEach
    void init() {
        fromUser = Member.builder()
                .name("from")
                .profileImageUrl("http://www.imagefrom.com")
                .role(Role.MEMBER).emailVerified(true)
                .build();
        toUser = Member.builder()
                .name("to")
                .profileImageUrl("http://www.imageto.com")
                .role(Role.MEMBER).emailVerified(true)
                .build();
        testUser = Member.builder()
                .name("amy")
                .profileImageUrl("http://www.imagetest.com")
                .role(Role.MEMBER).emailVerified(true)
                .build();

        memberRepository.saveAll(Arrays.asList(fromUser, toUser, testUser));
    }

    @Test
    @DisplayName("[성공] 팔로우 커스텀 레포지토리를 이용하여 팔로잉(내가 팔로우하는) 목록 조회, 사용자 name으로 정렬")
    void getFollowingsByMemberId_getListSizeOne_success() {
        //given
        followRepository.save(Follow.fromMembers(fromUser, toUser));
        followRepository.save(Follow.fromMembers(fromUser, testUser));

        //when
        List<FollowResponse> followings = followRepository.getFollowingsByMemberId(fromUser.getId());

        //then
        assertThat(followings).hasSize(2);
        assertThat(followings.get(0).getMember().getName()).isEqualTo(testUser.getName());
        assertThat(followings.get(1).getMember().getName()).isEqualTo(toUser.getName());
        logger.debug(() -> followings.get(0).toString());
    }

    @Test
    @DisplayName("[성공] 팔로우 커스텀 레포지토리를 이용하여 팔로워(나를 팔로우하는) 목록 조회, 사용자 name으로 정렬")
    void getFollowersByMemberId_getListSizeOne_success() {
        //given
        followRepository.save(Follow.fromMembers(fromUser, toUser));
        followRepository.save(Follow.fromMembers(testUser, toUser));

        //when
        List<FollowResponse> followers = followRepository.getFollowersByMemberId(toUser.getId());

        //then
        assertThat(followers).hasSize(2);
        assertThat(followers.get(0).getMember().getName()).isEqualTo(testUser.getName());
        assertThat(followers.get(1).getMember().getName()).isEqualTo(fromUser.getName());
        logger.debug(() -> followers.get(0).toString());
    }


}