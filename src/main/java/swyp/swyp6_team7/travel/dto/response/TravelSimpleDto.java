package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TravelSimpleDto {

    private int travelNumber;
    private String title;
    private String summary;
    private int userNumber;
    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime registerDue;
    private String postStatus;
    //TODO: 태그, 이미지


    @Builder
    public TravelSimpleDto(
            int travelNumber, String title, String summary,
            int userNumber, String userName,
            LocalDateTime createdAt, LocalDateTime registerDue, String postStatus
    ) {
        this.travelNumber = travelNumber;
        this.title = title;
        this.summary = summary;
        this.userNumber = userNumber;
        this.userName = userName;
        this.createdAt = createdAt;
        this.registerDue = registerDue;
        this.postStatus = postStatus;
    }

    public static TravelSimpleDto from(Travel travel) {
        return TravelSimpleDto.builder()
                .travelNumber(travel.getNumber())
                .title(travel.getTitle())
                .summary(travel.getSummary())
                .userNumber(travel.getUserNumber())
                //.userName(travel.getuserNam)
                .createdAt(travel.getCreatedAt())
                .registerDue(travel.getDueDateTime())
                .postStatus(travel.getStatus().getName())
                .build();
    }

}
