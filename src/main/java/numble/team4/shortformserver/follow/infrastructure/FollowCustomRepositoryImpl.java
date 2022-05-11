package numble.team4.shortformserver.follow.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import numble.team4.shortformserver.follow.ui.dto.FollowResponse;
import numble.team4.shortformserver.follow.ui.dto.QFollowResponse;
import numble.team4.shortformserver.follow.ui.dto.QFollowResponse_MemberDto;

import java.util.List;

import static numble.team4.shortformserver.follow.domain.QFollow.follow;

@RequiredArgsConstructor
public class FollowCustomRepositoryImpl implements FollowCustomRepository{

    private final JPAQueryFactory factory;

    @Override
    public List<FollowResponse> getFollowersByMemberId(Long id) {
        return factory.select(
                        new QFollowResponse(follow.id,
                                new QFollowResponse_MemberDto(
                                        follow.fromMember.id,
                                        follow.fromMember.name,
                                        follow.fromMember.profileImageUrl
                                ))
                )
                .from(follow)
                .where(follow.toMember.id.eq(id))
                .orderBy(follow.fromMember.name.asc())
                .fetch();

    }

    @Override
    public List<FollowResponse> getFollowingsByMemberId(Long id) {
        return factory.select(
                        new QFollowResponse(follow.id,
                                new QFollowResponse_MemberDto(
                                        follow.toMember.id,
                                        follow.toMember.name,
                                        follow.toMember.profileImageUrl
                                ))
                )
                .from(follow)
                .where(follow.fromMember.id.eq(id))
                .orderBy(follow.toMember.name.asc())
                .fetch();
    }
}
