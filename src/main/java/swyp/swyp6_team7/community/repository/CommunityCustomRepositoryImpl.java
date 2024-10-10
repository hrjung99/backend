package swyp.swyp6_team7.community.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static swyp.swyp6_team7.community.domain.QCommunity.community;



@Repository
public class CommunityCustomRepositoryImpl implements CommunityCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public CommunityCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    // 조회수 +1 하는 메소드
    @Override
    public void incrementViewCount(int postNumber) {
        queryFactory.update(community)
                .set(community.viewCount, community.viewCount.add(1))
                .where(community.postNumber.eq(postNumber))
                .execute();
    }
}