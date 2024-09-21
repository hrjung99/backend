package swyp.swyp6_team7.travel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GenderType {

    MIXED("혼성"),
    WOMAN_ONLY("여자만"),
    MAN_ONLY("남자만"),
    NONE("없음");

    private final String description;

    public static GenderType of(String type) {
        return Arrays.stream(values())
                .filter(e -> e.description.equals(type))
                .findFirst()
                .orElse(NONE);
    }

    @Override
    public String toString() {
        return description;
    }

}
