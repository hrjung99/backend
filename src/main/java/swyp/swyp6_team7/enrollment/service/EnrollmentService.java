package swyp.swyp6_team7.enrollment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentCreateRequest;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;
import swyp.swyp6_team7.travel.dto.response.TravelEnrollmentsResponse;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final TravelRepository travelRepository;
    private final MemberService memberService;


    @Transactional
    public void create(EnrollmentCreateRequest request, String email) {

        Users user = memberService.findByEmail(email);
        Travel targetTravel = travelRepository.findByNumber(request.getTravelNumber())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 콘텐츠입니다."));

        if (!targetTravel.availableForEnroll()) {
            throw new IllegalArgumentException("참가 신청 할 수 없는 상태의 콘텐츠 입니다.");
        }
        Enrollment created = request.toEntity(user.getUserNumber());
        enrollmentRepository.save(created);
    }

    @Transactional
    public void delete(long enrollmentNumber) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

        authorizeEnrollmentOwner(enrollment);
        enrollmentRepository.delete(enrollment);
    }

    public TravelEnrollmentsResponse findEnrollmentsByTravelNumber(int travelNumber) {
        Travel targetTravel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 콘텐츠입니다."));
        authorizeTravelHost(targetTravel);

        List<EnrollmentResponse> enrollments = enrollmentRepository.findEnrollmentsByTravelNumber(travelNumber);
        return TravelEnrollmentsResponse.from(enrollments);
    }

    private void authorizeEnrollmentOwner(Enrollment enrollment) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = memberService.findByEmail(userName);
        if (enrollment.getUserNumber() != user.getUserNumber()) {
            throw new IllegalArgumentException("접근 권한이 없는 신청서입니다.");
        }
    }

    private void authorizeTravelHost(Travel targetTravel) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = memberService.findByEmail(userName);
        if (!targetTravel.isUserTravelHost(user)) {
            throw new IllegalArgumentException("여행 주최자의 권한이 필요한 작업입니다.");
        }
    }

}
