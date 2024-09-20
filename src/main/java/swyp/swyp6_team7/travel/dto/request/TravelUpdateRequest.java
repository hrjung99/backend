package swyp.swyp6_team7.travel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelUpdateRequest {

    private String title;
    private String summary;
    @NotNull
    @Builder.Default
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
    private Boolean completionStatus;

}
