package swyp.swyp6_team7.bookmark.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookmarks")
@Getter
@Setter
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookmarkId;

    @Column(nullable = false)
    private Integer userNumber;

    @Column(nullable = false)
    private Integer contentId; // 여행 또는 커뮤니티 컨텐츠 ID, number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;  // TRAVEL 또는 COMMUNITY

    @Column(nullable = false)
    private LocalDateTime bookmarkDate = LocalDateTime.now();

    public Bookmark() {}

    public Bookmark(Integer userNumber, Integer contentId, ContentType contentType) {
        this.userNumber = userNumber;
        this.contentId = contentId;
        this.contentType = contentType;
    }
}
