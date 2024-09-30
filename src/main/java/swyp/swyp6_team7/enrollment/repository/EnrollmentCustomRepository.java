package swyp.swyp6_team7.enrollment.repository;

import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;

import java.util.List;

public interface EnrollmentCustomRepository {

    List<EnrollmentResponse> findEnrollmentsByTravelNumber(int travelNumber);

    List<EnrollmentResponse> findEnrollmentsByUserNumber(int userNumber);

}
