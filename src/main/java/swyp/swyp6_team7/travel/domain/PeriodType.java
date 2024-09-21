package swyp.swyp6_team7.travel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeriodType {

    ONE_WEEK("일주일 이하"),
    TWO_WEEKS("1~2주"),
    THREE_WEEKS("3~4주"),
    MORE_THAN_MONTH("한 달 이상");

    private final String description;

    @Override
    public String toString() {
        return description;
    }

}
