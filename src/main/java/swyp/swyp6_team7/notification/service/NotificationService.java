package swyp.swyp6_team7.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.notification.dto.NotificationDto;
import swyp.swyp6_team7.notification.dto.TravelNotificationDto;
import swyp.swyp6_team7.notification.entity.Notification;
import swyp.swyp6_team7.notification.entity.TravelNotification;
import swyp.swyp6_team7.notification.repository.NotificationRepository;
import swyp.swyp6_team7.notification.util.NotificationMaker;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;


    @Async
    public void createEnrollNotificaton(Travel targetTravel) {
        Notification newNotification = NotificationMaker.travelEnrollmentMessage(targetTravel);
        newNotification = notificationRepository.save(newNotification);
        log.info("[알림] 참가신청 =" + newNotification.toString());
    }

    @Async
    public void createAcceptNotification(Travel targetTravel, Enrollment enrollment) {
        Notification newNotification = NotificationMaker.travelAcceptMessage(targetTravel, enrollment);
        newNotification = notificationRepository.save(newNotification);
        log.info("[알림] 신청수락 = " + newNotification.toString());
    }

    @Async
    public void createRejectNotification(Travel targetTravel, Enrollment enrollment) {
        Notification newNotification = NotificationMaker.travelRejectMessage(targetTravel, enrollment);
        newNotification = notificationRepository.save(newNotification);
        log.info("[알림] 신청거절 = " + newNotification.toString());
    }


    public Page<NotificationDto> getNotificationsByUser(PageRequest pageRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = memberService.findByEmail(userName);

        Page<Notification> notifications = notificationRepository
                .getNotificationsByReceiverNumberOrderByIsReadAscCreatedAtDesc(user.getUserNumber(), pageRequest);

        return notifications.map(notification -> makeDto(notification));
    }

    private NotificationDto makeDto(Notification notification) {
        NotificationDto result;

        if (notification instanceof TravelNotification) {
            TravelNotification travelNotification = (TravelNotification) notification;
            result = new TravelNotificationDto(travelNotification);
        } else {
            result = new NotificationDto(notification);
        }
        changeReadStatus(notification);
        return result;
    }

    private void changeReadStatus(Notification notification) {
        if (!notification.getIsRead()) {
            notification.read();
        }
    }

}
