package swyp.swyp6_team7.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookmarkResponse {
    private Integer bookmarkId;
    private boolean bookmarked;
    private String title;
    private String location;
    private String username;
    private String postedAgo;
    private String dday;
    private int currentApplicants;
    private int maxPerson;
    private boolean completionStatus;
    private List<String> tags;
    private String detailUrl;
    private String removeBookmarkUrl;



    // 디데이 포맷 설정
    public static String formatDDay(LocalDate dueDate){
        long daysDifference = ChronoUnit.DAYS.between(LocalDate.now(),dueDate);
        return (daysDifference >= 0) ? "마감 D-" + daysDifference : "종료됨";
    }
    // 작성일 포맷 설정
    public static String formatPostedAgo(LocalDate postedDate){
        long daysDifference = ChronoUnit.DAYS.between(postedDate, LocalDate.now());
        if (daysDifference == 0) {
            return "오늘";
        } else if (daysDifference > 0) {
            return daysDifference + "일 전";
        } else {
            return -daysDifference + "일 후";
        }
    }

}
