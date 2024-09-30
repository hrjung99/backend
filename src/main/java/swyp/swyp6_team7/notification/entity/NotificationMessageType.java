package swyp.swyp6_team7.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessageType {

    TRAVEL_ENROLL("여행 신청 알림"),
    TRAVEL_ACCEPT("참가 확정 알림"),
    TRAVEL_REJECT("참가 거절 알림");

    private final String title;

}
