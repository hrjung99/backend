package swyp.swyp6_team7.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;

    @Column(nullable = false)
    private Integer userNumber;

    @Column(nullable = false)
    private Integer travelNumber;

    @Column(nullable = false)
    private LocalDateTime bookmarkDate = LocalDateTime.now();

    @Builder
    public Bookmark(Integer userNumber, Integer travelNumber, LocalDateTime bookmarkDate) {
        if (travelNumber == null || travelNumber <= 0) {
            throw new IllegalArgumentException("유효하지 않은 여행 번호입니다.");
        }
        this.userNumber = userNumber;
        this.travelNumber = travelNumber;
        this.bookmarkDate = bookmarkDate;
    }

}
