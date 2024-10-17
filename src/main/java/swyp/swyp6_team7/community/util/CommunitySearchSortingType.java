package swyp.swyp6_team7.community.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CommunitySearchSortingType {

    REG_DATE_DESC("최신순"),   //최근에 등록한 순서
    REG_DATE_ASC("등록일순"),   //먼저 등록한 순서

    VIEW_COUNT_DESC("조회순"), //조회수 많은 순서
    VIEW_COUNT_ASC("조회역순"), //조회수 적은 순서

    LIKE_COUNT_DESC("추천순"), //좋아요 많은 순서
    LIKE_COUNT_ASC("추천역순"); //좋아요 적은 순서


    private final String description;

    public static CommunitySearchSortingType of(String type) {

        if (type == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.description.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Sorting Type provided."));
    }
}