package swyp.swyp6_team7.travel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class TravelUpdateRequest {

    private String title;
    private String summary;
    //TODO: List<Tag> tags
    private String details;
    private LocalDateTime dueDateTime;
    private LocalDate travelStartAt;
    private LocalDate travelEndAt;
    private String location;
    private int minPerson;
    private int maxPerson;
    private int budget;
    @NotNull
    private Boolean completionStatus;

}
