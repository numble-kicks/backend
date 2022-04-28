package numble.team4.shortformserver.follow.application;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.exception.NotExistMemberException;
<<<<<<< HEAD
import org.junit.jupiter.api.BeforeAll;
=======
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private MemberRepository memberRepository;

<<<<<<< HEAD
    private static final Long fromId = 10L;
    private static final Long toId = 11L;

    private static Member fromUser;
    private static Member toUser;

    @BeforeAll
    static void init() {
        fromUser = Member.builder()
                .id(fromId).emailVerified(true).build();
        toUser = Member.builder()
                .id(toId).emailVerified(true).build();
=======
    Member makeTestToUser() {
        return Member.builder()
                .id(10L).emailVerified(true).build();
    }

    Member makeTestFromUser() {
        return Member.builder()
                .id(11L).emailVerified(true).build();
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
    }

    @Nested
    @DisplayName("팔로우 생성 테스트")
    class saveFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 팔로우 요청")
        void createFollow_notException_success() {
            //given
<<<<<<< HEAD
            Follow follow = Follow.fromMembers(fromUser, toUser);

            when(memberRepository.findById(toId)).thenReturn(Optional.of(toUser));
            when(followRepository.existsByFromMember_IdAndToMember_Id(fromId, toId))
=======
            Member fromUser = makeTestFromUser();
            Member toUser = makeTestToUser();
            Follow follow = Follow.fromMembers(fromUser, toUser);

            when(memberRepository.findById(toUser.getId())).thenReturn(Optional.of(toUser));
            when(followRepository.existsByFromMember_IdAndToMember_Id(fromUser.getId(), toUser.getId()))
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
                    .thenReturn(false);
            when(followRepository.save(any())).thenReturn(follow);

            //when
<<<<<<< HEAD
            followService.createFollow(fromUser, toId);
=======
            followService.createFollow(fromUser, toUser.getId());
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)

            //then
            verify(followRepository, times(1)).save(any());
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 사용자를 팔로우")
        void createFollow_notExistMemberException_fail() {
            //given
            when(memberRepository.findById(any())).thenReturn(Optional.ofNullable(null));

            //when, then
            assertThrows(NotExistMemberException.class,
<<<<<<< HEAD
                    () -> followService.createFollow(fromUser, toId));
=======
                    () -> followService.createFollow(any(), 1L));
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
        }

        @Test
        @DisplayName("[실패] 이미 팔로우 한 사용자를 또 팔로우")
        void createFollow_AlreadyExistFollowException_fail() {
            //given
<<<<<<< HEAD
            when(memberRepository.findById(toId)).thenReturn(Optional.of(toUser));
            when(followRepository.existsByFromMember_IdAndToMember_Id(fromId, toId))
=======
            Member fromUser = makeTestFromUser();
            Member toUser = makeTestToUser();

            when(memberRepository.findById(toUser.getId())).thenReturn(Optional.of(toUser));
            when(followRepository.existsByFromMember_IdAndToMember_Id(fromUser.getId(), toUser.getId()))
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
                    .thenReturn(true);

            //when, then
            assertThrows(AlreadyExistFollowException.class,
<<<<<<< HEAD
                    () -> followService.createFollow(fromUser, toId)
=======
                    () -> followService.createFollow(fromUser, toUser.getId())
>>>>>>> 143a669 (test: 요청된 팔로우 생성 service 테스트 추가)
            );
        }
    }
}