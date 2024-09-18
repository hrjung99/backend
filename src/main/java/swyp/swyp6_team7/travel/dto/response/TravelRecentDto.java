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

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelRecentDto {

    private static final int TAG_MAX_NUMBER = 3;

    @NotNull
    private int travelNumber;
    private String title;
    private String summary;
    private int userNumber;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime registerDue;
    private int maxPerson;
    private int nowPerson;
    private List<String> tags;
    //TODO: 이미지


    @Builder
    @QueryProjection
    public TravelRecentDto(
            int travelNumber, String title, String summary, int userNumber, String userName,
            LocalDateTime createdAt, LocalDateTime registerDue, int maxPerson, int nowPerson,
            List<String> tags) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.summary = summary;
        this.userNumber = userNumber;
        this.userName = userName;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.maxPerson = maxPerson;
        this.nowPerson = nowPerson;
        this.tags = tags;
    }


    public TravelRecentDto(Travel travel, List<Tag> tags) {
        this.travelNumber = travel.getNumber();
        this.title = travel.getTitle();
        this.summary = travel.getSummary();
        this.userNumber = travel.getUserNumber();
        this.userName = "testuser"; //todo
        this.createdAt = travel.getCreatedAt();
        this.registerDue = travel.getDueDateTime();
        this.maxPerson = travel.getMaxPerson();
        this.nowPerson = 1; //todo
        this.tags = convertToTagNames(tags);
    }

    private List<String> convertToTagNames(List<Tag> tags) {
        return tags.stream()
                .limit(TAG_MAX_NUMBER)
                .map(tag -> tag.getName())
                .toList();
    }

    @Override
    public String toString() {
        return "TravelRecentResponse{" +
                "travelNumber=" + travelNumber +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", userNumber=" + userNumber +
                ", userName='" + userName + '\'' +
                ", createdAt=" + createdAt +
                ", registerDue=" + registerDue +
                ", maxPerson=" + maxPerson +
                ", nowPerson=" + nowPerson +
                ", tags=" + tags +
                '}';
    }
}
