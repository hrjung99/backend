package swyp.swyp6_team7.notification.util;

import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.notification.entity.Notification;
import swyp.swyp6_team7.notification.entity.NotificationType;
import swyp.swyp6_team7.notification.entity.TravelNotification;
import swyp.swyp6_team7.travel.domain.Travel;

public class NotificationMaker {

    public static Notification travelEnrollmentMessage(Travel targetTravel, Users user) {
        return TravelNotification.builder()
                .receiverNumber(targetTravel.getUserNumber())
                .title(NotificationType.TRAVEL_ENROLL.getTitle())
                .content(user.getUserName() + "님이 참가를 희망했어요.\n수락하시려면 눌러주세요.")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

    public static Notification travelAcceptMessage(Travel targetTravel, Enrollment enrollment) {
        return TravelNotification.builder()
                .receiverNumber(enrollment.getUserNumber())
                .title(NotificationType.TRAVEL_ACCEPT.getTitle())
                .content("여행 신청이 수락되었어요.")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

    public static Notification travelRejectMessage(Travel targetTravel, Enrollment enrollment) {
        return TravelNotification.builder()
                .receiverNumber(enrollment.getUserNumber())
                .title(NotificationType.TRAVEL_REJECT.getTitle())
                .content("여행 신청이 거절되었어요.\n다른 여행을 찾아보세요!")
                .travelNumber(targetTravel.getNumber())
                .travelTitle(targetTravel.getTitle())
                .travelDueDate(targetTravel.getDueDate())
                .isRead(false)
                .build();
    }

}
