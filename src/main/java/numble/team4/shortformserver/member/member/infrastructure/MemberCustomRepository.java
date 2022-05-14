package numble.team4.shortformserver.member.member.infrastructure;

import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCustomRepository {
    Page<Member> findAllMembersByKeyword(String keyword, Pageable pageable);
}
