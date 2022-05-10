package numble.team4.shortformserver.member.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.team4.shortformserver.member.auth.domain.MemberAdapter;
import numble.team4.shortformserver.member.auth.exception.EmailEmptyException;
import numble.team4.shortformserver.member.member.domain.Member;
import numble.team4.shortformserver.member.member.domain.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private static final String USER_NAME_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NAME_NOT_FOUND));
        if (member.hasNotEmail())
            throw new EmailEmptyException();
        return new MemberAdapter(member);
    }
}
