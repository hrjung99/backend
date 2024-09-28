package swyp.swyp6_team7.travel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@AllArgsConstructor
public class TravelListResponseDto {
    private int travelNumber;
    private String title;
    private String location;
    private String username;
    private String dDay; // 마감 기한 (D-Day 형식)
    private String postedAgo; // 작성일로부터 경과한 시간
    private int currentApplicants; // 현재 신청 인원 수
    private int maxPerson; //최대 인원 수
    private boolean completionStatus;
    private List<String> tags;
    private String detailUrl; // 세부내용 조회 URL
    private String updateUrl; // 게시글 수정 URL
    private String deleteUrl; // 게시글 삭제 URL

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
