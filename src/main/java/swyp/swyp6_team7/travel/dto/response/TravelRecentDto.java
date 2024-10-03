package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TravelRecentDto {

    private static final int TAG_MAX_NUMBER = 3;

    @NotNull
    private int travelNumber;
    private String title;
    private String location;
    private int userNumber;
    private String userName;
    private List<String> tags;
    private int nowPerson;
    private int maxPerson;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registerDue;


    @Builder
    public TravelRecentDto(
            int travelNumber, String title, String location, int userNumber, String userName,
            List<String> tags, int nowPerson, int maxPerson,
            LocalDateTime createdAt, LocalDate registerDue
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
    }

    @QueryProjection
    public TravelRecentDto(
            Travel travel, int userNumber, String userName,
            int companionCount, List<String> tags
    ) {
        this.travelNumber = travel.getNumber();
        this.title = travel.getTitle();
        this.location = travel.getLocation();
        this.userNumber = userNumber;
        this.userName = userName;
        this.tags = tags.stream().limit(TAG_MAX_NUMBER).toList();
        this.nowPerson = companionCount;
        this.maxPerson = travel.getMaxPerson();
        this.createdAt = travel.getCreatedAt();
        this.registerDue = travel.getDueDate();
    }

}
