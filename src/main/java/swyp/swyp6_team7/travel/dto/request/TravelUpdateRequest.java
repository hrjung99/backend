package swyp.swyp6_team7.travel.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelUpdateRequest {
    private String locationName;
    @Size(max = 20)
    private String title;
    private String details;
    @PositiveOrZero
    private int maxPerson;
    private String genderType;
    @FutureOrPresent
    private LocalDate dueDate;
    private String periodType;
    @NotNull
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    @NotNull
    private Boolean completionStatus;

    public Travel toTravelEntity(int userNumber, Location location) {
        return Travel.builder()
                .userNumber(userNumber)
                .locationName(location.getLocationName())  // location 설정
                .title(title)
                .details(details)
                .viewCount(0)
                .maxPerson(maxPerson)
                .genderType(GenderType.of(genderType))
                .dueDate(dueDate)
                .periodType(PeriodType.of(periodType))
                .status(TravelStatus.IN_PROGRESS)
                .build();
    }

}
