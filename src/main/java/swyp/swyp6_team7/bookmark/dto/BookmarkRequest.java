package swyp.swyp6_team7.bookmark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkRequest {
    private Integer userNumber;
    private Integer contentId;
    private String contentType;

    public BookmarkRequest(Integer userNumber, Integer contentId,String contentType) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.userNumber = userNumber;
    }
}
