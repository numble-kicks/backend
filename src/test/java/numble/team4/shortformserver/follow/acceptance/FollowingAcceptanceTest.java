package numble.team4.shortformserver.follow.acceptance;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.member.member.domain.Role;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static numble.team4.shortformserver.common.exception.ExceptionType.*;
import static numble.team4.shortformserver.follow.ui.FollowResponseMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowingAcceptanceTest extends BaseAcceptanceTest {

    private static final String baseUri = "/v1/users/following";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowRepository followRepository;

    private Member user1;
    private Member user2;

    @BeforeEach
    void 초기_설정_user1_user2_생성() {
        user1 = Member.builder().name("user1").role(Role.MEMBER).build();
        user2 = Member.builder().name("user2").role(Role.MEMBER).build();
        memberRepository.saveAll(Arrays.asList(user1, user2));
    }

    @Nested
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 1. 본인이 아닌 다른 사용자를 팔로우한 상태에서 취소 요청")
        void deleteFollow_isok_success () throws Exception {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(user1, user2));

            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{followId}", follow.getId())
                    .queryParam("from_member", String.valueOf(user1.getId()))
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
                    .queryParam("from_member", String.valueOf(user1.getId()))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_FOLLOW.getMessage()));
        }

        @Test
        @DisplayName("[실패] 2. 본인이 생성하지 않은 팔로우에 대해 취소 요청")
        void deleteFollow_notFollowingException_fail() throws Exception {
            //given
            Follow follow = followRepository.save(Follow.fromMembers(user1, user2));

            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{followId}", follow.getId())
                    .queryParam("from_member", String.valueOf(user2.getId()))
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
            Follow follow = followRepository.save(Follow.fromMembers(user1, user2));

            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/from")
                    .queryParam("from_member", String.valueOf(user1.getId()))
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
            Follow follow = followRepository.save(Follow.fromMembers(user1, user2));

            //when
            ResultActions res = mockMvc.perform(get(baseUri + "/to")
                    .queryParam("to_member", String.valueOf(user2.getId()))
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
