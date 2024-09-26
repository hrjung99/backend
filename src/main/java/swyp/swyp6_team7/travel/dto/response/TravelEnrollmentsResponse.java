package swyp.swyp6_team7.travel.dto.response;

import lombok.Builder;
import lombok.Getter;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;

import java.util.List;

@Getter
public class TravelEnrollmentsResponse {

    private int totalCount;
    private List<EnrollmentResponse> enrollments;

    @Builder
    public TravelEnrollmentsResponse(int totalCount, List<EnrollmentResponse> enrollments) {
        this.totalCount = totalCount;
        this.enrollments = enrollments;
    }

    public static TravelEnrollmentsResponse from(List<EnrollmentResponse> enrollments) {
        return TravelEnrollmentsResponse.builder()
                .totalCount(enrollments.size())
                .enrollments(enrollments)
                .build();
    }

}
