package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelRecentResponse {

    @NotNull
    private int travelNumber;
    private String title;
    private String summary;
    private int userNumber;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY년 MM월 dd일")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd일")
    private LocalDateTime registerDue;
    private int maxPerson;
    private int nowPerson;
    private List<String> tags;
    //TODO: 이미지


    @Builder
    public TravelRecentResponse(
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

}
