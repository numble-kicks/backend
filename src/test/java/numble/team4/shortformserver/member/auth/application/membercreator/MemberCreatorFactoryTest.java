package numble.team4.shortformserver.member.auth.application.membercreator;

import numble.team4.shortformserver.member.auth.domain.OauthProvider;
import numble.team4.shortformserver.member.member.domain.Member;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberCreatorFactoryTest {
    private final MemberCreatorFactory factory = new MemberCreatorFactory(new HashSet<>(List.of(new TestMemberCreator())));

    @Test
    void findClientTest() {
        MemberCreatorFromSocialInfo testMemberCreator = factory.findMemberCreator("kakao");
        assertThat(testMemberCreator.getProviderName()).isEqualTo(OauthProvider.KAKAO);
    }

    static class TestMemberCreator implements MemberCreatorFromSocialInfo {
        @Override
        public Member signUpOrLoginMember(String response) {
            return null;
        }

        @Override
        public OauthProvider getProviderName() {
            return OauthProvider.KAKAO;
        }
    }
}