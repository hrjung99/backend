package swyp.swyp6_team7.travel.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelDetailResponse {

    private int travelId;
    private String title;
    private String summary;
    private int userNumber;
    private String userName;
    private String details;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDate travelStartAt;
    private LocalDate travelEndAt;
    private LocalDateTime registerDue;
    private String location;
    private int minPerson;
    private int maxPerson;
    private int budget;
    private String postStatus;
    //TODO: Image 처리, 현재 모집 확정된 인원수 처리


    @Builder
    public TravelDetailResponse(
            int travelId, String title, String summary,
            int userNumber, String userName, String details, int viewCount,
            LocalDateTime createdAt, LocalDate travelStartAt, LocalDate travelEndAt,
            LocalDateTime registerDue, String location, int minPerson, int maxPerson,
            int budget, String postStatus
    ) {
        this.travelId = travelId;
        this.title = title;
        this.summary = summary;
        this.userNumber = userNumber;
        this.userName = userName;
        this.details = details;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.travelStartAt = travelStartAt;
        this.travelEndAt = travelEndAt;
        this.registerDue = registerDue;
        this.location = location;
        this.minPerson = minPerson;
        this.maxPerson = maxPerson;
        this.budget = budget;
        this.postStatus = postStatus;
    }

    public static TravelDetailResponse from(
            Travel travel,
            int userNumber, String userName
    ) {
        return TravelDetailResponse.builder()
                .travelId(travel.getId())
                .title(travel.getTitle())
                .summary(travel.getSummary())
                .userNumber(userNumber)
                .userName(userName)
                .details(travel.getDetails())
                .viewCount(travel.getViewCount())
                .createdAt(travel.getCreatedAt())
                .travelStartAt(travel.getStartAt())
                .travelEndAt(travel.getEndAt())
                .registerDue(travel.getDueDateTime())
                .location(travel.getLocation())
                .minPerson(travel.getMinPerson())
                .maxPerson(travel.getMaxPerson())
                .budget(travel.getBudget())
                .postStatus(travel.getStatus().getName())
                .build();
    }
}