package swyp.swyp6_team7.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
@AllArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;

    @Column(nullable = false)
    private Integer userNumber;

    @Column(nullable = false)
    private Integer travelNumber; // 여행 또는 커뮤니티 컨텐츠 ID, number

    @Column(nullable = false)
    private LocalDateTime bookmarkDate = LocalDateTime.now();

    public Bookmark() {}

    public Bookmark(Integer userNumber, Integer travelNumber, LocalDateTime bookmarkDate) {
        this.userNumber = userNumber;
        this.travelNumber = travelNumber;
        this.bookmarkDate = bookmarkDate;
    }

}
