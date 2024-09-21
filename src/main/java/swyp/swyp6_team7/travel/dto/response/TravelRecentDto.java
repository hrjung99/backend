package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.tag.domain.Tag;
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
    private int userNumber;
    private String userName;
    private List<String> tags;
    private int nowPerson;
    private int maxPerson;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDate registerDue;
    private String postStatus;


    @Builder
    @QueryProjection
    public TravelRecentDto(
            int travelNumber, String title, int userNumber, String userName,
            List<String> tags, int nowPerson, int maxPerson,
            LocalDateTime createdAt, LocalDate registerDue, String postStatus
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


    public TravelRecentDto(Travel travel, List<Tag> tags) {
        this.travelNumber = travel.getNumber();
        this.title = travel.getTitle();
        this.userNumber = travel.getUserNumber();
        this.userName = "testuser"; //todo
        this.tags = convertToTagNames(tags);
        this.nowPerson = 1; //todo
        this.maxPerson = travel.getMaxPerson();
        this.createdAt = travel.getCreatedAt();
        this.registerDue = travel.getDueDate();
    }

    private List<String> convertToTagNames(List<Tag> tags) {
        return tags.stream()
                .limit(TAG_MAX_NUMBER)
                .map(tag -> tag.getName())
                .toList();
    }

}
