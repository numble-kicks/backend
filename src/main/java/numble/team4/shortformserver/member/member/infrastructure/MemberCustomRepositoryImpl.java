package numble.team4.shortformserver.member.member.infrastructure;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.member.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static numble.team4.shortformserver.member.member.domain.QMember.member;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory factory;

    @Override
    public Page<Member> findAllMembersByKeyword(String keyword, Pageable pageable) {
        List<Member> members = factory.selectFrom(member)
                .where(checkContainsKeyword(keyword))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(members, pageable, members::size);
    }

    BooleanExpression checkContainsKeyword(String keyword) {
        if (Objects.isNull(keyword)) {
            return Expressions.asBoolean(true).isTrue();
        }
        return member.email.contains(keyword).or(member.name.contains(keyword));
    }
}
