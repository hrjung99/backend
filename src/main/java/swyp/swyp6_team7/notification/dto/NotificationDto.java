package swyp.swyp6_team7.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import swyp.swyp6_team7.notification.entity.Notification;

import java.time.LocalDateTime;

@Getter
public class NotificationDto {

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private String content;

    private Boolean isRead;


    public NotificationDto(Notification notification) {
        this.title = notification.getTitle();
        this.createdAt = notification.getCreatedAt();
        this.content = notification.getContent();
        this.isRead = notification.getIsRead();
    }
}
