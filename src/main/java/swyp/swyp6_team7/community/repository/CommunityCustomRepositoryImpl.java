package swyp.swyp6_team7.community.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.category.domain.QCategory;
import swyp.swyp6_team7.community.domain.QCommunity;
import swyp.swyp6_team7.community.dto.response.CommunitySearchCondition;
import swyp.swyp6_team7.community.dto.response.CommunitySearchDto;
import swyp.swyp6_team7.community.dto.response.QCommunitySearchDto;
import swyp.swyp6_team7.community.util.CommunitySearchSortingType;
import swyp.swyp6_team7.likes.domain.QLike;
import swyp.swyp6_team7.member.entity.QUsers;

import java.util.ArrayList;
import java.util.List;


@Repository
public class CommunityCustomRepositoryImpl implements CommunityCustomRepository {

    private final JPAQueryFactory queryFactory;
    QCommunity community = QCommunity.community;
    QUsers users = QUsers.users;
    QCategory categories = QCategory.category;
    QLike like = QLike.like;


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

    public Page<CommunitySearchDto> search(PageRequest pageRequest, CommunitySearchCondition searchCondition) {
        List<CommunitySearchDto> content = queryFactory
                .select(new QCommunitySearchDto(
                        community,
                        users.userName,
                        categories.categoryName,
                        like.count().as("likeCount")
                ))
                .from(community)
                .leftJoin(users).on(community.userNumber.eq(users.userNumber))
                .leftJoin(categories).on(community.categoryNumber.eq(categories.categoryNumber))
                .leftJoin(like).on(like.relatedType.eq("community")
                        .and(like.relatedNumber.eq(community.postNumber)))
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        categoryEquals(searchCondition.getCategoryNumber())
                )
                .orderBy(getOrderBy(searchCondition.getSortingType()).toArray(new OrderSpecifier[0]))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(community.count())
                .from(community)
                .leftJoin(users).on(community.userNumber.eq(users.userNumber))
                .leftJoin(categories).on(community.categoryNumber.eq(categories.categoryNumber))
                .leftJoin(like).on(like.relatedType.eq("community")
                        .and(like.relatedNumber.eq(community.postNumber)))
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        categoryEquals(searchCondition.getCategoryNumber())
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(content, searchCondition.getPageRequest(), () -> totalCount);
    }

    // 키워드 검색 조건
    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null) {
            return null;
        }
        // OR 조건으로 title과 content에서 검색
        return community.title.containsIgnoreCase(keyword)
                .or(community.content.containsIgnoreCase(keyword));
    }

    // 카테고리 필터 조건
    private BooleanExpression categoryEquals(Integer categoryNumber) {
        // categoryNumber가 null일 경우 모든 카테고리에 대해 조회
        return categoryNumber != null ? community.categoryNumber.eq(categoryNumber) : community.categoryNumber.isNotNull();
    }


    // 정렬 조건 설정
    private List<OrderSpecifier<?>> getOrderBy(CommunitySearchSortingType sortingType) {

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortingType == null) {
            orderSpecifiers.add(community.regDate.desc());
        } else {
            switch (sortingType) {
                case REG_DATE_DESC:
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(community.viewCount.desc());
                    orderSpecifiers.add(like.count().desc());
                    break;
                case REG_DATE_ASC:
                    orderSpecifiers.add(community.regDate.asc());
                    orderSpecifiers.add(community.viewCount.desc());
                    orderSpecifiers.add(like.count().desc());
                    break;

                case VIEW_COUNT_DESC:
                    orderSpecifiers.add(community.viewCount.desc());
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(like.count().desc());
                    break;
                case VIEW_COUNT_ASC:
                    orderSpecifiers.add(community.viewCount.asc());
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(like.count().desc());
                    break;

                case LIKE_COUNT_DESC:
                    orderSpecifiers.add(like.count().desc());
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(community.viewCount.desc());
                    break;
                case LIKE_COUNT_ASC:
                    orderSpecifiers.add(like.count().asc());
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(community.viewCount.desc());
                    break;
                default:
                    orderSpecifiers.add(community.regDate.asc());
                    break;
            }
        }
        return orderSpecifiers;

    }
}