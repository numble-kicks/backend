package numble.team4.shortformserver.follow.acceptance;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.member.auth.domain.MemberAdapter;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import numble.team4.shortformserver.testCommon.mockUser.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static numble.team4.shortformserver.common.exception.ExceptionType.*;
import static numble.team4.shortformserver.follow.ui.FollowResponseMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowingAcceptanceTest extends BaseAcceptanceTest {

    private static final String baseUri = "/v1/users/following";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowRepository followRepository;

    private Member fromMember;
    private Member toMember;


    @BeforeEach
    void 초기_설정_user1_user2_생성() {
        fromMember = Member.builder().name("user2").role(Role.MEMBER).build();
        toMember = Member.builder().name("user2").role(Role.MEMBER).build();
        memberRepository.saveAll(Arrays.asList(fromMember, toMember));
    }

    @Nested
    @WithMockCustomUser
    @DisplayName("팔로잉 여부 확인 테스트")
    class ExistFollowTest {

        @Test
        @DisplayName("[성공] 팔로잉하고 있는 사용자에 대해 팔로우 여부 확인")
        void existFollow_existFollow_isok_success() throws Exception {
            //given
            Member member = ((MemberAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
            Follow follow = Follow.fromMembers(member, toMember);
            followRepository.save(follow);

            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/users/following/{toUserId}", toMember.getId())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(GET_IS_EXIST_FOLLOW.getMessage()));
        }


        @Test
        @DisplayName("[성공] 팔로잉하고 있지 않는 사용자에 대해 팔로우 여부 확인")
        void existFollow_notExistFollow_isok_success() throws Exception {
            //when
            ResultActions res = mockMvc.perform(
                    get("/v1/users/following/{toUserId}", 23049823)
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(GET_IS_EXIST_FOLLOW.getMessage()));
        }
    }

    @Nested
    @WithMockCustomUser
    @DisplayName("팔로우 생성 테스트")
    class CreateFollowTest {

        @Test
        @DisplayName("[성공] 1. 본인이 아닌 다른 사용자를 팔로우 요청")
        void createFollow_isok_success() throws Exception {
            //when
            ResultActions res = mockMvc.perform(post("/v1/users/following/{toMemberId}", toMember.getId()));

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(CREATE_FOLLOW.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 1. 본인을 팔로우 요청")
        void createFollow_notSelfFollowableException_success() throws Exception {
            //when
            Member loginUser = ((MemberAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getMember();
            ResultActions res = mockMvc.perform(post("/v1/users/following/{toMemberId}", loginUser.getId()));

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_SELF_FOLLOW_ABLE.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 2. 이미 팔로우한 사용자에 대해 팔로우 요청")
        void createFollow_alreadyExistFollowException_fail() throws Exception {
            //given
            mockMvc.perform(post("/v1/users/following/{toMemberId}", toMember.getId()));

            //when
            ResultActions res = mockMvc.perform(post("/v1/users/following/{toMemberId}", toMember.getId()));

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(ALREADY_EXIST_FOLLOW.getMessage()));

        }
    }

    @Nested
    @WithMockCustomUser
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 1. 본인이 아닌 다른 사용자를 팔로우한 상태에서 취소 요청")
        void deleteFollow_isok_success () throws Exception {
            //given
            mockMvc.perform(post("/v1/users/following/{toMemberId}", toMember.getId()));
            Follow follow = followRepository.findAll().get(0);

            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{followId}", follow.getId())
                    .queryParam("from_member", String.valueOf(fromMember.getId()))
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(DELETE_FOLLOW.getMessage()))
                    .andDo(print());
            assertThat(followRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("[실패] 1. 존재하지 않는 팔로우에 대해 팔로우 취소 요청")
        void deleteFollow_notExistMemberException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{followId}", 999)
                    .queryParam("from_member", String.valueOf(fromMember.getId()))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_FOLLOW.getMessage()));
        }

        @Test
        @DisplayName("[실패] 2. 본인이 생성하지 않은 팔로우에 대해 취소 요청")
        void deleteFollow_notFollowingException_fail() throws Exception {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{followId}", follow.getId())
                    .queryParam("from_member", String.valueOf(toMember.getId()))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOLLOWING.getMessage()));
        }
    }
    
    @Nested
    @DisplayName("팔로우 목록 조회 테스트")
    class GetFollowsTest {
        
        @Test
        @DisplayName("[성공] 1. [팔로잉] 존재하는 사용자의 팔로잉 목로 조회 요청")
        void getAllFollowings_isok_success() throws Exception {
            //given
            followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/from")
                    .queryParam("from_member", String.valueOf(fromMember.getId()))
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()").value(1))
                    .andExpect(jsonPath("$.message").value(GET_FOLLOWINGS.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 2. [팔로잉] 존재하지 않는 사용자의 팔로잉 목록 조회 요청")
        void getAllFollowings_notExistMemberException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/from")
                    .queryParam("from_member", String.valueOf(12312L))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[성공] 1. [팔로워] 존재하는 사용자의 팔로워 목로 조회 요청")
        void getAllFollowers_isok_success() throws Exception {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(fromMember, toMember));

            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/to")
                    .queryParam("to_member", String.valueOf(toMember.getId()))
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.size()").value(1))
                    .andExpect(jsonPath("$.message").value(GET_FOLLOWERS.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 2. [팔로워] 존재하지 않는 사용자의 팔로워 목록 조회 요청")
        void getAllFollowers_notExistMemberException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/to")
                    .queryParam("to_member", String.valueOf(12312))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()))
                    .andDo(print());

        }
    }
}
