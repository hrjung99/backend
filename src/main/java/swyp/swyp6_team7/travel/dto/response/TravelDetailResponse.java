package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelDetailResponse {

    private int travelNumber;
    private int userNumber;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private String location;
    private String title;
    private String details;
    private int viewCount;
    private int nowPerson;
    private int maxPerson;
    private String genderType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String periodType;
    private List<String> tags;
    private String postStatus;
    //TODO: 신청수, 관심수
    //TODO: 주최자여부, 신청가능여부

    @Builder
    public TravelDetailResponse(
            int travelNumber, int userNumber, String userName,
            LocalDateTime createdAt, String location, String title,
            String details, int viewCount, int nowPerson, int maxPerson, String genderType,
            LocalDate dueDate, String periodType, List<String> tags,
            String postStatus
    ) {
        this.travelNumber = travelNumber;
        this.userNumber = userNumber;
        this.userName = userName;
        this.createdAt = createdAt;
        this.location = location;
        this.title = title;
        this.details = details;
        this.viewCount = viewCount;
        this.nowPerson = nowPerson;
        this.maxPerson = maxPerson;
        this.genderType = genderType;
        this.dueDate = dueDate;
        this.periodType = periodType;
        this.tags = tags;
        this.postStatus = postStatus;
    }

    @QueryProjection
    public TravelDetailResponse(
            Travel travel, int userNumber, String userName,
            List<String> tags
    ) {
        this.travelNumber = travel.getNumber();
        this.userNumber = userNumber;
        this.userName = userName;
        this.createdAt = travel.getCreatedAt();
        this.location = travel.getLocation();
        this.title = travel.getTitle();
        this.details = travel.getDetails();
        this.viewCount = getViewCount();
        this.nowPerson = travel.getCompanions().size();
        this.maxPerson = travel.getMaxPerson();
        this.genderType = travel.getGenderType().toString();
        this.dueDate = travel.getDueDate();
        this.periodType = travel.getPeriodType().toString();
        this.tags = tags;
        this.postStatus = travel.getStatus().toString();
    }

    public static TravelDetailResponse from(
            Travel travel,
            List<String> tags,
            int userNumber, String userName
    ) {
        return TravelDetailResponse.builder()
                .travelNumber(travel.getNumber())
                .userNumber(userNumber)
                .userName(userName)
                .createdAt(travel.getCreatedAt())
                .location(travel.getLocation())
                .title(travel.getTitle())
                .details(travel.getDetails())
                .viewCount(travel.getViewCount())
                .nowPerson(travel.getCompanions().size())
                .maxPerson(travel.getMaxPerson())
                .genderType(travel.getGenderType().toString())
                .dueDate(travel.getDueDate())
                .periodType(travel.getPeriodType().toString())
                .tags(tags)
                .postStatus(travel.getStatus().toString())
                .build();
    }

    @Override
    public String toString() {
        return "TravelDetailResponse{" +
                "travelNumber=" + travelNumber +
                ", userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                ", createdAt=" + createdAt +
                ", location='" + location + '\'' +
                ", title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", viewCount=" + viewCount +
                ", nowPerson=" + nowPerson +
                ", maxPerson=" + maxPerson +
                ", genderType='" + genderType + '\'' +
                ", dueDate=" + dueDate +
                ", periodType='" + periodType + '\'' +
                ", tags=" + tags +
                ", postStatus='" + postStatus + '\'' +
                '}';
    }
}