package swyp.swyp6_team7.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING, length = 20)
@Entity
public abstract class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_number")
    private Long number;

    @CreatedDate
    @Column(name = "notification_create_time", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "notification_receiver_number", nullable = false)
    private Integer receiverNumber;

    @Column(name = "notification_title", length = 20)
    private String title;

    @Column(name = "notification_content", length = 100)
    private String content;

    @Column(name = "notification_read", nullable = false)
    private Boolean isRead;

    //TODO: 이미지


    public Notification(
            Long number, LocalDateTime createdAt, Integer receiverNumber,
            String title, String content, Boolean isRead
    ) {
        this.number = number;
        this.createdAt = createdAt;
        this.receiverNumber = receiverNumber;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
    }

    public void read() {
        this.isRead = true;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "number=" + number +
                ", createdAt=" + createdAt +
                ", receiverNumber=" + receiverNumber +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                '}';
    }

}
