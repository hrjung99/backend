package swyp.swyp6_team7.travel.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
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
public class TravelRecommendDto {

    private static final int RECOMMEND_TAG_MAX_NUMBER = 3;

    @NotNull
    private int travelNumber;
    private String title;
    private String location;
    private int userNumber;
    private String userName;
    private List<String> tags;
    private int nowPerson;
    private int maxPerson;
    private LocalDateTime createdAt;
    private LocalDate registerDue;
    private int preferredNumber;

    @Builder
    public TravelRecommendDto(
            int travelNumber, String title, String location, int userNumber, String userName,
            List<String> tags, int nowPerson, int maxPerson,
            LocalDateTime createdAt, LocalDate registerDue, int preferredNumber
    ) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.location = location;
        this.userNumber = userNumber;
        this.userName = userName;
        this.tags = tags;
        this.nowPerson = nowPerson;
        this.maxPerson = maxPerson;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.preferredNumber = preferredNumber;
    }

    @QueryProjection
    public TravelRecommendDto(
            Travel travel, int userNumber, String userName,
            int companionCount, List<String> tags
    ) {
        this.travelNumber = travel.getNumber();
        this.title = travel.getTitle();
        this.location = travel.getLocationName();
        this.userNumber = userNumber;
        this.userName = userName;
        this.tags = tags.stream()
                .limit(RECOMMEND_TAG_MAX_NUMBER).toList();
        this.nowPerson = companionCount;
        this.maxPerson = travel.getMaxPerson();
        this.createdAt = travel.getCreatedAt();
        this.registerDue = travel.getDueDate();
    }

    public void updatePreferredNumber(Integer number) {
        this.preferredNumber = number;
    }

    @Override
    public String toString() {
        return "TravelRecommendDto{" +
                "travelNumber=" + travelNumber +
                ", title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                ", tags=" + tags +
                ", nowPerson=" + nowPerson +
                ", maxPerson=" + maxPerson +
                ", createdAt=" + createdAt +
                ", registerDue=" + registerDue +
                ", preferredNumber=" + preferredNumber +
                '}';
    }
}

