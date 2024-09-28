package swyp.swyp6_team7.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> getNotificationsByReceiverNumberOrderByCreatedAtDesc(int userNumber);

    Page<Notification> getNotificationsByReceiverNumberOrderByCreatedAtDesc(int userNumber, Pageable pageable);

}
