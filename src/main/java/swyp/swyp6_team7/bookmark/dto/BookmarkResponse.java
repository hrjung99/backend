package swyp.swyp6_team7.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkResponse {
    private Integer bookmarkId;
    private Integer contentId;
    private String contentType;
    private LocalDateTime bookmarkDate;
    private String contentUrl;

    public BookmarkResponse(Integer bookmarkId, Integer contentId, String contentType, LocalDateTime bookmarkDate,String contentUrl) {
        this.bookmarkId = bookmarkId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.bookmarkDate = bookmarkDate;
        this.contentUrl = contentUrl;
    }
}
