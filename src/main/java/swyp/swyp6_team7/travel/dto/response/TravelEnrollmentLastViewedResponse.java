package swyp.swyp6_team7.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TravelEnrollmentLastViewedResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime lastViewedAt;

    public TravelEnrollmentLastViewedResponse(LocalDateTime lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }
}
