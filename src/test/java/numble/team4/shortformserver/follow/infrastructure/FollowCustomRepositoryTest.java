package numble.team4.shortformserver.follow.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import numble.team4.shortformserver.common.config.TestQueryDslConfig;
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
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@BaseDataJpaTest
@Import(TestQueryDslConfig.class)
class FollowCustomRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(FollowCustomRepositoryTest.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    private Member fromUser;
    private Member toUser;

    @BeforeEach
    void init() {
        fromUser = Member.builder()
                .name("from")
                .profileImageUrl("http://www.imagefrom.com")
                .role(Role.ROLE_MEMBER).emailVerified(true)
                .build();
        toUser = Member.builder()
                .name("to")
                .profileImageUrl("http://www.imageto.com")
                .role(Role.ROLE_MEMBER).emailVerified(true)
                .build();
        memberRepository.save(fromUser);
        memberRepository.save(toUser);
    }

    @Test
    @DisplayName("[성공] 팔로우 커스텀 레포지토리를 이용하여 팔로잉(내가 팔로우하는) 목록 조회")
    void getFollowingsByMemberId_getListSizeOne_success() {
        //given
        Follow follow = followRepository.save(Follow.fromMembers(fromUser, toUser));

        //when
        List<FollowResponse> followings = followRepository.getFollowingsByMemberId(toUser.getId());

        //then
        assertThat(followings).hasSize(1);
        assertThat(followings.get(0).getId()).isEqualTo(follow.getId());
        logger.debug(() -> followings.get(0).toString());
    }

    @Test
    @DisplayName("[성공] 팔로우 커스텀 레포지토리를 이용하여 팔로워(나를 팔로우하는) 목록 조회")
    void getFollowersByMemberId_getListSizeOne_success() {
        //given
        Follow follow = followRepository.save(Follow.fromMembers(fromUser, toUser));

        //when
        List<FollowResponse> followers = followRepository.getFollowersByMemberId(fromUser.getId());

        //then
        assertThat(followers).hasSize(1);
        assertThat(followers.get(0).getId()).isEqualTo(follow.getId());
        logger.debug(() -> followers.get(0).toString());
    }


}