package swyp.swyp6_team7.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnrollmentCreateRequest {

    @Positive
    private int travelNumber;

    @NotNull
    @Size(max = 1000)
    private String message;


    public EnrollmentCreateRequest(int travelNumber, String message) {
        this.travelNumber = travelNumber;
        this.message = message;
    }

    public Enrollment toEntity(int userNumber) {
        return Enrollment.builder()
                .userNumber(userNumber)
                .travelNumber(travelNumber)
                .message(message)
                .status(EnrollmentStatus.PENDING)
                .build();
    }

}
