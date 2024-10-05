package swyp.swyp6_team7.travel.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum TravelSearchSortingType {

    RECOMMEND("추천순"),   //즐겨 찾기 된 count
    CREATED_AT_DESC("최신순"),
    CREATED_AT_ASC("등록일순");
    //ACCURACY("정확도순");


    private final String description;

    public static TravelSearchSortingType of(String type) {
        if (type == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.description.equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Sorting Type provided."));
    }

}
