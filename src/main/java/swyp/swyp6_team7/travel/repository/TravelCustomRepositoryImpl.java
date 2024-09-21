package swyp.swyp6_team7.travel.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.member.entity.QUsers;
import swyp.swyp6_team7.tag.domain.QTag;
import swyp.swyp6_team7.tag.domain.QTravelTag;
import swyp.swyp6_team7.travel.domain.QTravel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.QTravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;
import swyp.swyp6_team7.travel.dto.response.TravelSearchDto;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Slf4j
@Repository
public class TravelCustomRepositoryImpl implements TravelCustomRepository {

    private final JPAQueryFactory queryFactory;

    public TravelCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QTravel travel = QTravel.travel;
    QUsers users = QUsers.users;
    QTag tag = QTag.tag;
    QTravelTag travelTag = QTravelTag.travelTag;


    @Override
    public TravelDetailResponse getDetailsByNumber(int travelNumber) {
        return queryFactory
                .select(travel)
                .from(travel)
                .leftJoin(users).on(travel.userNumber.eq(users.userNumber))
                .leftJoin(travel.travelTags, travelTag)
                .leftJoin(travelTag.tag, tag)
                .where(travel.number.eq(travelNumber))
                .transform(groupBy(travel.number).as(new QTravelDetailResponse(
                        travel,
                        users.userNumber,
                        users.userName,
                        list(tag.name)
                ))).get(travelNumber);
    }


    @Override
    public Page<TravelRecentDto> findAllSortedByCreatedAt(PageRequest pageRequest) {

        List<Integer> travels = queryFactory
                .select(travel.number)
                .from(travel)
                .where(
                        statusInProgress()
                )
                .orderBy(travel.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<TravelRecentDto> content = queryFactory
                .select(travel)
                .from(travel)
                .leftJoin(travel.travelTags, travelTag)
                .leftJoin(travelTag.tag, tag)
                .where(
                        travel.number.in(travels)
                )
                .orderBy(travel.createdAt.desc())
                .transform(groupBy(travel.number).list(
                        Projections.constructor(TravelRecentDto.class,
                                travel, list(tag)))
                );

        JPAQuery<Long> countQuery = queryFactory
                .select(travel.count())
                .from(travel)
                .where(
                        statusActivated()
                );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
    }


    @Override
    public Page<TravelSearchDto> search(TravelSearchCondition condition) {
        List<Integer> travels = queryFactory
                .select(travel.number)
                .from(travel)
                .leftJoin(travel.travelTags, travelTag)
                .leftJoin(travelTag.tag, tag)
                .where(
                        titleLike(condition.getKeyword()),
                        statusActivated(),
                        eqTags(condition.getTags())
                )
                .groupBy(travel.number)
                .having(tag.name.count().goe((long) condition.getTags().size()))
                .orderBy(travel.createdAt.desc())
                .offset(condition.getPageRequest().getOffset())
                .limit(condition.getPageRequest().getPageSize())
                .fetch();

        List<TravelSearchDto> content = queryFactory
                .select(travel)
                .from(travel)
                .leftJoin(travel.travelTags, travelTag)
                .leftJoin(travelTag.tag, tag)
                .where(
                        travel.number.in(travels)
                )
                .orderBy(travel.createdAt.desc())
                .transform(groupBy(travel.number).list(
                        Projections.constructor(TravelSearchDto.class,
                                travel, list(tag.name)))
                );


        JPAQuery<Long> countQuery = queryFactory
                .select(travel.count())
                .from(travel)
                .leftJoin(travel.travelTags, travelTag)
                .leftJoin(travelTag.tag, tag)
                .where(
                        titleLike(condition.getKeyword()),
                        statusActivated(),
                        eqTags(condition.getTags())
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

    private BooleanExpression statusInProgress() {
        return travel.status.eq(TravelStatus.IN_PROGRESS);
    }

    private BooleanExpression eqTags(List<String> tags) {
        if (tags.isEmpty()) {
            return null;
        }
        return tag.name.in(tags);
    }

}
