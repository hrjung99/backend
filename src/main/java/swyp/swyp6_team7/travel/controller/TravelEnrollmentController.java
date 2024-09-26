package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.travel.dto.response.TravelEnrollmentsResponse;
import swyp.swyp6_team7.enrollment.service.EnrollmentService;

@RequiredArgsConstructor
@RestController
public class TravelEnrollmentController {

    private final EnrollmentService enrollmentService;


    @GetMapping("/api/travel/{travelNumber}/enrollments")
    public ResponseEntity<TravelEnrollmentsResponse> findEnrollments(@PathVariable("travelNumber") int travelNumber) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(enrollmentService.findEnrollmentsByTravelNumber(travelNumber));
    }

}
