package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSearchDto {

    private static final int TAG_MAX_NUMBER = 3;

    private int travelNumber;
    private String title;
    private int userNumber;
    private String userName;
    private List<String> tags;
    private int nowPerson;
    private int maxPerson;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime registerDue;
    private String postStatus;
    //TODO: 이미지

    @Builder
    public TravelSearchDto(
            int travelNumber, String title, int userNumber, String userName,
            List<String> tags, int maxPerson, int nowPerson,
            LocalDateTime createdAt, LocalDateTime registerDue, String postStatus
    ) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.userNumber = userNumber;
        this.userName = userName;
        this.tags = tags;
        this.nowPerson = nowPerson;
        this.maxPerson = maxPerson;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.postStatus = postStatus;
    }

    @QueryProjection
    public TravelSearchDto(
            Travel travel,
            List<String> tags
    ) {
        this.travelNumber = travel.getNumber();
        this.title = travel.getTitle();
        this.userNumber = travel.getUserNumber();
        this.userName = "testuser";
        this.tags = tags.stream().limit(TAG_MAX_NUMBER).toList();
        this.nowPerson = 1;
        this.maxPerson = travel.getMaxPerson();
        this.createdAt = travel.getCreatedAt();
        this.registerDue = travel.getDueDateTime();
        this.postStatus = travel.getStatus().getName();
    }

    @Override
    public String toString() {
        return "TravelSearchDto{" +
                "travelNumber=" + travelNumber +
                ", title='" + title + '\'' +
                ", userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                ", tags=" + tags +
                ", nowPerson=" + nowPerson +
                ", maxPerson=" + maxPerson +
                ", createdAt=" + createdAt +
                ", registerDue=" + registerDue +
                ", postStatus='" + postStatus + '\'' +
                '}';
    }
}
