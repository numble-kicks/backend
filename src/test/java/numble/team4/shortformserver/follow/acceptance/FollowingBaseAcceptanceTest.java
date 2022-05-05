package numble.team4.shortformserver.follow.acceptance;

import numble.team4.shortformserver.follow.domain.Follow;
import numble.team4.shortformserver.follow.domain.FollowRepository;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import numble.team4.shortformserver.testCommon.BaseAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_MEMBER;
import static numble.team4.shortformserver.common.exception.ExceptionType.NOT_EXIST_FOLLOW;
import static numble.team4.shortformserver.follow.ui.FollowResponseMessage.DELETE_FOLLOW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowingBaseAcceptanceTest extends BaseAcceptanceTest {

    private static final String baseUri = "/v1/users/following";

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FollowRepository followRepository;

    private Member user1;
    private Member user2;

    @BeforeEach
    void 초기_설정_user1_user2_생성() {
        user1 = Member.builder().name("user1").build();
        user2 = Member.builder().name("user2").build();
        memberRepository.saveAll(Arrays.asList(user1, user2));
    }

    @Nested
    @DisplayName("팔로우 취소 테스트")
    class DeleteFollowTest {

        @Test
        @DisplayName("[성공] 1. 본인이 아닌 다른 사용자를 팔로우한 상태에서 취소 요청")
        void deleteFollow_isok_success () throws Exception {
            //given
            followRepository.save(Follow.fromMembers(user1, user2));

            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{toUserId}", user2.getId())
                    .queryParam("from_member", String.valueOf(user1.getId()))
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(DELETE_FOLLOW.getMessage()))
                    .andDo(print());
            assertThat(followRepository.count()).isEqualTo(0);
        }

        @Test
        @DisplayName("[실패] 1. 존재하지 않는 사용자에 대해 팔로우 취소 요청")
        void deleteFollow_notExistMemberException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{toUserId}", 999)
                    .queryParam("from_member", String.valueOf(user1.getId()))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_MEMBER.getMessage()));
        }

        @Test
        @DisplayName("[실패] 2. 팔로우하지 않는 사용자에 대해 팔로우 취소 요청")
        void deleteFollow_notFollowingException_fail() throws Exception {
            //when
            ResultActions res = mockMvc.perform(delete(baseUri + "/{toUserId}", user2.getId())
                    .queryParam("from_member", String.valueOf(user1.getId()))
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_EXIST_FOLLOW.getMessage()));
        }
    }
}
