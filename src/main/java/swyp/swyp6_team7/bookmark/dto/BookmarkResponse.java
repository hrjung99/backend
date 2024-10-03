package swyp.swyp6_team7.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class BookmarkResponse {
    private int travelNumber;           // 여행 번호
    private String title;               // 여행 제목
    private String locaiton;
    private int userNumber;             // 사용자 번호
    private String userName;            // 사용자 이름
    private List<String> tags;          // 태그 리스트
    private int nowPerson;              // 현재 참가 인원 수
    private int maxPerson;              // 최대 참가 인원 수
    private String createdAt;           // 작성일
    private String registerDue;         // 등록 마감일
    private boolean isBookmarked;       // 북마크 여부

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
    }

    public static TravelListResponseDto fromEntity(Travel travel, Users user, int currentApplicants, boolean isBookmarked) {
        return new TravelListResponseDto(
                travel.getNumber(),
                travel.getTitle(),
                travel.getLocation(),
                user.getUserNumber(),
                user.getUserName(),
                travel.getTravelTags().stream().map(tag -> tag.getTag().getName()).collect(Collectors.toList()),
                currentApplicants,
                travel.getMaxPerson(),
                formatDate(travel.getCreatedAt().toLocalDate()),
                formatDate(travel.getDueDate()),
                isBookmarked
        );
    }

}
