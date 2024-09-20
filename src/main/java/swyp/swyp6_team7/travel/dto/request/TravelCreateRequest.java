package swyp.swyp6_team7.travel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelCreateRequest {

    private String title;
    private String summary;
    @NotNull @Builder.Default
    private List<String> tags = new ArrayList<>();
    private String details;
    private LocalDateTime dueDateTime;
    private LocalDate travelStartAt;
    private LocalDate travelEndAt;
    private String location;
    private int minPerson;
    private int maxPerson;
    private int budget;
    @NotNull
    private boolean completionStatus;


    //@Builder
    public TravelCreateRequest(
            String title, String summary, List<String> tags, String details,
            LocalDateTime dueDateTime, LocalDate travelStartAt, LocalDate travelEndAt,
            String location, int minPerson, int maxPerson, int budget, boolean completionStatus
    ) {
        this.title = title;
        this.summary = summary;
        this.tags = tags;
        this.details = details;
        this.dueDateTime = dueDateTime;
        this.travelStartAt = travelStartAt;
        this.travelEndAt = travelEndAt;
        this.location = location;
        this.minPerson = minPerson;
        this.maxPerson = maxPerson;
        this.budget = budget;
        this.completionStatus = completionStatus;
    }


    public Travel toTravelEntity(int userNumber) {
        return Travel.builder()
                .userNumber(userNumber)
                .title(title)
                .summary(summary)
                .details(details)
                .viewCount(0)
                .startAt(travelStartAt)
                .endAt(travelEndAt)
                .dueDateTime(dueDateTime)
                .location(location)
                .minPerson(minPerson)
                .maxPerson(maxPerson)
                .budget(budget)
                .status(TravelStatus.convertCompletionToStatus(completionStatus))
                .build();
    }

}
