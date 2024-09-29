package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelDetailDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelDetailResponse {

    private int travelNumber;
    private int userNumber;     //주최자 번호
    private String userName;    //주최자 이름
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private String location;
    private String title;
    private String details;
    private int viewCount;      //조회수
    private int enrollCount;    //신청수
    private int bookmarkCount;  //관심수(북마크수)
    private int nowPerson;      //현재 모집 인원
    private int maxPerson;      //최대 모집 인원
    private String genderType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String periodType;
    private List<String> tags;
    private String postStatus;
    private boolean hostUser;        //주최자 여부
    private boolean enrollAvailable; //신청 가능 여부

    @Builder
    public TravelDetailResponse(
            int travelNumber, int userNumber, String userName, LocalDateTime createdAt, String location,
            String title, String details, int viewCount, int enrollCount, int bookmarkCount,
            int nowPerson, int maxPerson, String genderType, LocalDate dueDate, String periodType,
            List<String> tags, String postStatus, boolean hostUser, boolean enrollAvailable
    ) {
        this.travelNumber = travelNumber;
        this.userNumber = userNumber;
        this.userName = userName;
        this.createdAt = createdAt;
        this.location = location;
        this.title = title;
        this.details = details;
        this.viewCount = viewCount;
        this.enrollCount = enrollCount;
        this.bookmarkCount = bookmarkCount;
        this.nowPerson = nowPerson;
        this.maxPerson = maxPerson;
        this.genderType = genderType;
        this.dueDate = dueDate;
        this.periodType = periodType;
        this.tags = tags;
        this.postStatus = postStatus;
        this.hostUser = hostUser;
        this.enrollAvailable = enrollAvailable;
    }

    public TravelDetailResponse(TravelDetailDto travelDetail) {
        this.travelNumber = travelDetail.getTravel().getNumber();
        this.userNumber = travelDetail.getHostNumber();
        this.userName = travelDetail.getHostName();
        this.createdAt = travelDetail.getTravel().getCreatedAt();
        this.location = travelDetail.getTravel().getLocation();
        this.title = travelDetail.getTravel().getTitle();
        this.details = travelDetail.getTravel().getDetails();
        this.viewCount = getViewCount();
        this.nowPerson = travelDetail.getTravel().getCompanions().size();
        this.maxPerson = travelDetail.getTravel().getMaxPerson();
        this.genderType = travelDetail.getTravel().getGenderType().toString();
        this.dueDate = travelDetail.getTravel().getDueDate();
        this.periodType = travelDetail.getTravel().getPeriodType().toString();
        this.tags = travelDetail.getTags();
        this.postStatus = travelDetail.getTravel().getStatus().toString();
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
                ", enrollCount=" + enrollCount +
                ", bookmarkCount=" + bookmarkCount +
                ", nowPerson=" + nowPerson +
                ", maxPerson=" + maxPerson +
                ", genderType='" + genderType + '\'' +
                ", dueDate=" + dueDate +
                ", periodType='" + periodType + '\'' +
                ", tags=" + tags +
                ", postStatus='" + postStatus + '\'' +
                ", hostUser=" + hostUser +
                ", enrollAvailable=" + enrollAvailable +
                '}';
    }
}