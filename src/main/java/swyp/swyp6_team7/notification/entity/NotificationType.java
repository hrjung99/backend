package swyp.swyp6_team7.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    TRAVEL_ENROLL("여행 참가 신청"),
    TRAVEL_ACCEPT("여행 참가 신청"),
    TRAVEL_REJECT("여행 참가 신청");

    private final String title;

}
