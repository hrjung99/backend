package swyp.swyp6_team7.notification.util;

import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.notification.entity.Notification;
import swyp.swyp6_team7.notification.entity.NotificationMessageType;
import swyp.swyp6_team7.notification.entity.TravelNotification;
import swyp.swyp6_team7.travel.domain.Travel;

public class NotificationMaker {

    public static Notification travelEnrollmentMessage(Travel targetTravel) {
        return TravelNotification.builder()
                .receiverNumber(targetTravel.getUserNumber())
                .title(NotificationMessageType.TRAVEL_ENROLL.getTitle())
                .content(targetTravel.getTitle() + "에 참가 신청자가 있어요. 알림을 눌러 확인해보세요.")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

    public static Notification travelAcceptMessage(Travel targetTravel, Enrollment enrollment) {
        return TravelNotification.builder()
                .receiverNumber(enrollment.getUserNumber())
                .title(NotificationMessageType.TRAVEL_ACCEPT.getTitle())
                .content(targetTravel.getTitle() + "에 참가가 확정되었어요. 멤버 댓글을 통해 인사를 나눠보세요.")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

    public static Notification travelRejectMessage(Travel targetTravel, Enrollment enrollment) {
        return TravelNotification.builder()
                .receiverNumber(enrollment.getUserNumber())
                .title(NotificationMessageType.TRAVEL_REJECT.getTitle())
                .content(targetTravel.getTitle() + "에 참가가 아쉽게도 거절되었어요. 다른 여행을 찾아볼까요?")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

}
