package swyp.swyp6_team7.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.notification.dto.NotificationDto;
import swyp.swyp6_team7.notification.service.NotificationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/api/notifications")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUser() {
        List<NotificationDto> notifications = notificationService.getNotificationsByUser();

        return ResponseEntity.status(HttpStatus.OK)
                .body(notifications);
    }

}
