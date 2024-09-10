package swyp.swyp6_team7.travel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelStatus {
    DRAFT("임시 저장"),
    IN_PROGRESS("모집 중"),
    CLOSED("모집 종료"),
    DELETED("삭제");


    private final String name;
}
