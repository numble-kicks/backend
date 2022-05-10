package numble.team4.shortformserver.member.auth.application.membercreator;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoMemberCreatorTest {

    @InjectMocks
    private KakaoMemberCreator creator;

    @Mock
    private MemberRepository memberRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Test
    void getProviderTest() {
        assertThat(creator.getProviderName()).isEqualTo(OauthProvider.KAKAO);
    }

    @DisplayName("카카오 Auth server로부터 받은 응답으로 회원가입 또는 로그인 테스트")
    @Test
    void signUpOrLoginMemberTest() {
        String response = "{\n" +
                "    \"id\": 12341234,\n" +
                "    \"connected_at\": \"2022-04-17T14:47:39Z\",\n" +
                "    \"properties\": {\n" +
                "        \"nickname\": \"numble\"\n" +
                "    },\n" +
                "    \"kakao_account\": {\n" +
                "        \"profile_nickname_needs_agreement\": false,\n" +
                "        \"profile_image_needs_agreement\": false,\n" +
                "        \"profile\": {\n" +
                "            \"nickname\": \"numble\",\n" +
                "            \"is_default_image\": true\n" +
                "        },\n" +
                "        \"has_email\": true,\n" +
                "        \"email_needs_agreement\": false,\n" +
                "        \"is_email_valid\": true,\n" +
                "        \"is_email_verified\": true,\n" +
                "        \"email\": \"numble@numble.com\",\n" +
                "        \"has_birthday\": true,\n" +
                "        \"birthday_needs_agreement\": false,\n" +
                "        \"birthday\": \"0101\",\n" +
                "        \"birthday_type\": \"SOLAR\",\n" +
                "        \"has_gender\": true,\n" +
                "        \"gender_needs_agreement\": false,\n" +
                "        \"gender\": \"male\"\n" +
                "    }\n" +
                "}";
        when(memberRepository.findByEmail("numble@numble.com"))
                .thenReturn(Optional.of(new Member("numble@numble.com", "numble", MEMBER, false)));

        Member newMember = creator.signUpOrLoginMember(response);

        assertThat(newMember).isEqualTo(new Member("numble@numble.com", "numble", MEMBER, false));
    }
}