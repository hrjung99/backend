package swyp.swyp6_team7.travel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderType {

    MIXED("혼성"),
    WOMAN_ONLY("여자만"),
    MAN_ONLY("남자만");

    private final String name;

}
