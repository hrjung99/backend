package swyp.swyp6_team7.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.notification.dto.NotificationDto;
import swyp.swyp6_team7.notification.service.NotificationService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/api/notifications")
    public ResponseEntity<Page<NotificationDto>> getNotificationsByUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<NotificationDto> notifications = notificationService.getNotificationsByUser(PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK)
                .body(notifications);
    }

}
