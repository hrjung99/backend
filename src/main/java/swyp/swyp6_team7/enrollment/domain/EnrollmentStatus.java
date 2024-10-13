package swyp.swyp6_team7.enrollment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnrollmentStatus {

    PENDING("대기"),
    ACCEPTED("수락"),
    REJECTED("거절");

    private final String description;


    @Override
    public String toString() {
        return description;
    }
}
