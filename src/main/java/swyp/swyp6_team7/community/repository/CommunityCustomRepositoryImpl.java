package swyp.swyp6_team7.community.repository;

import com.querydsl.core.Tuple;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        // 1. 좋아요 수 집계 쿼리
        Map<Integer, Long> likeCountMap = getLikeCountByPostNumbers();

        // 2. 게시글과 관련된 정보 조회 + 좋아요 수 매핑
        List<CommunitySearchDto> content = queryFactory
                .select(community, users.userName, categories.categoryName)
                .from(community)
                .leftJoin(users).on(community.userNumber.eq(users.userNumber))
                .leftJoin(categories).on(community.categoryNumber.eq(categories.categoryNumber))
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        categoryEquals(searchCondition.getCategoryNumber())
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> new CommunitySearchDto(
                        tuple.get(community),
                        tuple.get(users.userName),
                        tuple.get(categories.categoryName),
                        likeCountMap.getOrDefault(tuple.get(community.postNumber), 0L)
                ))
                .toList();

        // 3. 전체 게시글 수 조회
        Long totalCount = queryFactory
                .select(community.count())
                .from(community)
                .where(
                        keywordContains(searchCondition.getKeyword()),
                        categoryEquals(searchCondition.getCategoryNumber())
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(content, searchCondition.getPageRequest(), () -> totalCount);
    }

    private Map<Integer, Long> getLikeCountByPostNumbers() {
        List<Tuple> likeCounts = queryFactory
                .select(like.relatedNumber, like.count())
                .from(like)
                .where(like.relatedType.eq("community"))
                .groupBy(like.relatedNumber)
                .fetch();

        // 결과를 Map으로 변환
        Map<Integer, Long> likeCountMap = new HashMap<>();
        for (Tuple tuple : likeCounts) {
            likeCountMap.put(tuple.get(like.relatedNumber), tuple.get(like.count()));
        }
        return likeCountMap;
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
    private List<OrderSpecifier<?>> getOrderBy(CommunitySearchSortingType sortingType, Map<Integer, Long> likeCountMap) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (sortingType == null) {
            orderSpecifiers.add(community.regDate.desc());
        } else {
            switch (sortingType) {
                case REG_DATE_DESC:
                    orderSpecifiers.add(community.regDate.desc());
                    orderSpecifiers.add(community.viewCount.desc());
                    break;
                case REG_DATE_ASC:
                    orderSpecifiers.add(community.regDate.asc());
                    orderSpecifiers.add(community.viewCount.desc());
                    break;
                case VIEW_COUNT_DESC:
                    orderSpecifiers.add(community.viewCount.desc());
                    orderSpecifiers.add(community.regDate.desc());
                    break;
                case VIEW_COUNT_ASC:
                    orderSpecifiers.add(community.viewCount.asc());
                    orderSpecifiers.add(community.regDate.desc());
                    break;
                case LIKE_COUNT_DESC:
                    orderSpecifiers.add(getLikeCountOrderSpecifier(likeCountMap, false));
                    orderSpecifiers.add(community.regDate.desc());
                    break;
                case LIKE_COUNT_ASC:
                    orderSpecifiers.add(getLikeCountOrderSpecifier(likeCountMap, true));
                    orderSpecifiers.add(community.regDate.desc());
                    break;
                default:
                    orderSpecifiers.add(community.regDate.asc());
                    break;
            }
        }
        return orderSpecifiers;
    }

    // 좋아요 수에 따른 정렬 조건 생성
    private OrderSpecifier<Long> getLikeCountOrderSpecifier(Map<Integer, Long> likeCountMap, boolean ascending) {
        // Map에 저장된 좋아요 수를 기준으로 정렬 (커스텀 정렬)
        return new OrderSpecifier<>(
                ascending ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                com.querydsl.core.types.dsl.Expressions.numberTemplate(Long.class,
                        "function('coalesce', {0}, 0)", likeCountMap)
        );
    }
}