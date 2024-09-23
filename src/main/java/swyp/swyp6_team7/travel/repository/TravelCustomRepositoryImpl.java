package swyp.swyp6_team7.travel.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.travel.domain.QTravel;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelSimpleDto;

import java.util.List;

@Repository
public class TravelCustomRepositoryImpl implements TravelCustomRepository {

    private final JPAQueryFactory queryFactory;

    public TravelCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QTravel travel = QTravel.travel;


    @Override
    public Page<Travel> search(TravelSearchCondition condition) {
        List<Travel> content = queryFactory
                .select(travel)
                .from(travel)
                .where(
                        titleLike(condition.getKeyword()),
                        statusActivated()
                )
                .orderBy(travel.createdAt.desc())
                .offset(condition.getPageRequest().getOffset())
                .limit(condition.getPageRequest().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(travel.count())
                .from(travel)
                .where(
                        titleLike(condition.getKeyword()),
                        statusActivated()
                );

        return PageableExecutionUtils.getPage(content, condition.getPageRequest(), countQuery::fetchOne);
    }

    private BooleanExpression titleLike(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) {
            return null;
        }
        return travel.title.contains(keyword);
    }

    private BooleanExpression statusActivated() {
        return travel.status.eq(TravelStatus.IN_PROGRESS)
                .or(travel.status.eq(TravelStatus.CLOSED));
    }

}