package numble.team4.shortformserver.testCommon;

import numble.team4.shortformserver.member.auth.domain.MemberAdapter;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@BaseDataJpaTest
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {

        Member member = Member.builder()
                .email(withMockCustomUser.email())
                .name(withMockCustomUser.name())
                .role(withMockCustomUser.role())
                .emailVerified(withMockCustomUser.emailVerified())
                .build();

        memberRepository.save(member);

        UserDetails userDetails = new MemberAdapter(member);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        return context;
    }
}
