package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchDto {

    private int travelNumber;
    private String title;
    private int userNumber;
    private String userName;
    private List<String> tags;
    //private int nowPerson;
    private int maxPerson;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime registerDue;
    private String postStatus;
    //TODO: 이미지

    @Builder
    public TravelSearchDto(
            int travelNumber, String title,  int userNumber,  //String userName,
            List<String> tags, int maxPerson,
            LocalDateTime createdAt, LocalDateTime registerDue, String postStatus
    ) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.userNumber = userNumber;
        //this.userName = userName;
        this.tags = tags;
        //현재 인원
        this.maxPerson = maxPerson;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.postStatus = postStatus;
    }

    @QueryProjection
    public TravelSearchDto(
            int travelNumber, String title,  int userNumber,  //String userName,
            List<String> tags, int maxPerson,
            LocalDateTime createdAt, LocalDateTime registerDue, TravelStatus postStatus
    ) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.userNumber = userNumber;
        //this.userName = userName;
        this.tags = tags;
        //현재 인원
        this.maxPerson = maxPerson;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.postStatus = postStatus.getName();
    }

    public static TravelSearchDto from(Travel travel) {
        return TravelSearchDto.builder()
                .travelNumber(travel.getNumber())
                .title(travel.getTitle())
                .userNumber(travel.getUserNumber())
                //.userName(travel.getuserNam)
                //.tags(tags)
                .maxPerson(travel.getMaxPerson())
                .createdAt(travel.getCreatedAt())
                .registerDue(travel.getDueDateTime())
                .postStatus(travel.getStatus().getName())
                .build();
    }

    @Override
    public String toString() {
        return "TravelSearchDto{" +
                "travelNumber=" + travelNumber +
                ", title='" + title + '\'' +
                ", userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                ", tags=" + tags +
                '}';
    }
}
