package numble.team4.shortformserver.member.auth.application;

import numble.team4.shortformserver.member.auth.application.membercreator.KakaoMemberCreator;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static numble.team4.shortformserver.member.member.domain.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
public class AuthTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private KakaoMemberCreator kakaoMemberCreator;

    @Test
    void emailEmptyTest() {
        memberRepository.save(new Member(null, "testUser1", MEMBER, false));
        memberRepository.save(new Member(null, "testUser2", MEMBER, false));
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
                "        \"has_birthday\": true,\n" +
                "        \"birthday_needs_agreement\": false,\n" +
                "        \"birthday\": \"0101\",\n" +
                "        \"birthday_type\": \"SOLAR\",\n" +
                "        \"has_gender\": true,\n" +
                "        \"gender_needs_agreement\": false,\n" +
                "        \"gender\": \"male\"\n" +
                "    }\n" +
                "}";
        Member member = kakaoMemberCreator.signUpOrLoginMember(response);
        //assertThat(member).isEqualTo(new Member(null, "numble", ROLE_MEMBER, false));
    }
}
