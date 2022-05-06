package numble.team4.shortformserver.follow.ui;

import numble.team4.shortformserver.common.config.SecurityConfig;
import numble.team4.shortformserver.follow.application.FollowService;
import numble.team4.shortformserver.follow.exception.AlreadyExistFollowException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static numble.team4.shortformserver.follow.ui.FollowResponseMessage.CREATE_FOLLOW;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = FollowController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
@MockBean(JpaMetamodelMappingContext.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private FollowService followService;

    Member makeTestFromUser() {
        return Member.builder()
                .id(11L).emailVerified(true).build();
    }

    @Nested
    @WithMockUser(roles = "USER")
    @DisplayName("팔로우 생성 api 테스트")
    @WithMockUser(roles = "USER")
    class createFollowTest {

        @Test
        @DisplayName("[성공] 정상적인 팔로우 생성 요청")
        void createFollow_ok_success() throws Exception {
            //given
            Member fromUser = makeTestFromUser();
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(fromUser));

            //when
            ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/following/{to_member}", "1")
                    .queryParam("from_member", "11"));

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(CREATE_FOLLOW.getMessage()))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 팔로우하려는 사용자가 존재하지 않는 사용자일 때")
        void createFollow_notExistMemberException_fail() throws Exception {
            //given
            when(memberRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

            //when
            ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/following/{to_member}", "1")
                    .queryParam("from_member", "2"));

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."))
                    .andDo(print());
        }

        @Test
        @DisplayName("[실패] 이미 팔로우가 되어 있는 사용자를 팔로우 하려고 할 때")
        void createFollow_alreadyExistFollowException_fail() throws Exception {
            //given
            Member fromUser = makeTestFromUser();
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(fromUser));

            doThrow(new AlreadyExistFollowException())
                    .when(followService).createFollow(any(Member.class), anyLong());

            //when
            ResultActions res = mockMvc.perform(MockMvcRequestBuilders.get("/v1/users/following/{to_member}", "1")
                    .queryParam("from_member", "2"));

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("이미 팔로우가 되어있는 사용자입니다."))
                    .andDo(print());
        }
    }

}