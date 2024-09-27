package swyp.swyp6_team7.bookmark.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkRequest {
    private Integer userNumber;
    private Integer contentId;
    private String contentType;
}
