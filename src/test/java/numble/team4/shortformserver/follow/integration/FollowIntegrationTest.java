package numble.team4.shortformserver.follow.integration;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.follow.exception.NotExistFollowException;
import numble.team4.shortformserver.follow.exception.NotFollowingException;
import numble.team4.shortformserver.follow.exception.NotSelfFollowableException;
import numble.team4.shortformserver.follow.ui.FollowController;
import numble.team4.shortformserver.follow.ui.dto.FollowExistResponse;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
import numble.team4.shortformserver.testCommon.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.junit.jupiter.api.Assertions.*;

@BaseIntegrationTest
public class FollowIntegrationTest {

    @Autowired
    private FollowController followController;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private EntityManager entityManager;

    private Member fromMember;
    private Member toMember;

    @BeforeEach
    void init() {
        fromMember = Member.builder().name("from").role(Role.MEMBER).build();
        toMember = Member.builder().name("to").role(Role.MEMBER).build();

        memberRepository.saveAll(Arrays.asList(fromMember, toMember));
    }
    
    @Nested
    @DisplayName("특정 사용자 팔로우 여부 확인 테스트")
    class ExistFollowTest {

        @Test
        @DisplayName("[성공] 팔로우하고 있을 때")
        void existFollow_returnValueIsTrueAndHasId_success() {
            //given
            Follow follow = Follow.fromMembers(fromMember, toMember);
            followRepository.save(follow);
            entityManager.flush();
            entityManager.clear();

            //when
            FollowExistResponse existFollowInfo = followController.existFollow(fromMember, toMember.getId()).getData();

            //then
            assertTrue(existFollowInfo.isExistFollow());
            assertThat(existFollowInfo.getFollowId()).isEqualTo(follow.getId());
        }


        @Test
        @DisplayName("[성공] 팔로우하고 있지 않을 때")
        void existFollow_returnValueIsFalse_success() {
            //when
            FollowExistResponse existFollowInfo = followController.existFollow(fromMember, 2309489023L).getData();

            //then
            assertFalse(existFollowInfo.isExistFollow());
            assertThat(existFollowInfo.getFollowId()).isNull();
        }
    }

    @Nested
    @DisplayName("팔로우 생성 테스트")
    class SaveFollowTest {

        @Test
        @DisplayName("[성공] 본인이 팔로우 하지 않은 사용자에 팔로우 요청")
        void createFollow_followRepositoryFindAllHasSizeOne_success() {
            //when
            followController.createFollow(fromMember, toMember.getId());

            //then
            assertThat(followRepository.count()).isEqualTo(1L);
        }

        @Test
        @DisplayName("[실패] 본인을 팔로우하도록 요청")
        void createFollow_notSelfFollowableException_fail() {
            //when, then
            assertThrows(
                    NotSelfFollowableException.class,
                    () -> followController.createFollow(fromMember, fromMember.getId())
            );
        }

        @Test
        @DisplayName("[실패] 이미 팔로우한 사용자에 대해 팔로우 요청")
        void createFollow_alreadyExistFollowException_fail () {
            //given
            Follow follow = Follow.fromMembers(fromMember, toMember);
            followRepository.save(follow);

            //when, then
            assertThrows(
                    AlreadyExistFollowException.class,
                    () -> followController.createFollow(fromMember, toMember.getId())
            );
        }
    }

    @Nested
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 본인 외의 다른 사용자를 팔로우 한 상태에서 취소 요청")
        void deleteFollow_isok_success() {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            followController.deleteFollow(fromMember , follow.getId());

            //then
            assertThat(followRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 팔로우에 대해 취소 요청")
        void deleteFollow_notExistMemberException_fail() {
            //when, then
            assertThrows(
                    NotExistFollowException.class,
                    () -> followController.deleteFollow(fromMember, 9999L)
            );
        }

        @Test
        @DisplayName("[실패] 다른 사람이 생성한 팔로우에 대해 취소 요청")
        void deleteFollow_notFollowingException_fail() {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when, then
            assertThrows(
                    NotFollowingException.class,
                    () -> followController.deleteFollow(toMember, follow.getId())
            );
        }
    }

    @Nested
    @DisplayName("팔로우 목록 조회 테스트")
    class GetFollowsTest {

        @Test
        @DisplayName("[성공] 팔로잉(내가 팔로우하는) 목록 조회")
        void getAllFollowings_getListSizeOne_success() {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            followController.getAllFollowings(fromMember.getId());

            //then
            assertThat(followRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("[성공] 팔로워(나를 팔로우하는) 목록 조회")
        void getAllFollowers_getListSizeOne_success() {
            //given
            followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            followController.getAllFollowers(toMember.getId());

            //then
            assertThat(followRepository.count()).isEqualTo(1);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 사용자의 팔로잉 목록 요청")
        void getAllFollowings_notExistMemberException_fail() {
            //when, then
            assertThrows(
                    NotExistMemberException.class,
                    () -> followController.getAllFollowings(13L)
            );
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 사용자의 팔로워 목록 요청")
        void getAllFollowerss_notExistMemberException_fail() {
            //when, then
            assertThrows(
                    NotExistMemberException.class,
                    () -> followController.getAllFollowers(13L)
            );
        }
    }
}
