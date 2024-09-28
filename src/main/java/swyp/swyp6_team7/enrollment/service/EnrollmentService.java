package swyp.swyp6_team7.enrollment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentCreateRequest;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.notification.entity.Notification;
import swyp.swyp6_team7.notification.repository.NotificationRepository;
import swyp.swyp6_team7.notification.util.NotificationMaker;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.response.TravelEnrollmentsResponse;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final TravelRepository travelRepository;
    private final CompanionRepository companionRepository;
    private final NotificationRepository notificationRepository;
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

        //알림
        Notification newNotification = NotificationMaker.travelEnrollmentMessage(targetTravel, user);
        newNotification = notificationRepository.save(newNotification);
        log.info("참가 신청 알림 생성: " + newNotification.toString());
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

    @Transactional
    public void accept(long enrollmentNumber) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

        Travel targetTravel = travelRepository.findByNumber(enrollment.getTravelNumber())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 콘텐츠입니다."));
        authorizeTravelHost(targetTravel);

        if (!targetTravel.availableForAddCompanion()) {
            throw new IllegalArgumentException("모집 인원이 마감되었습니다.");
        }
        enrollment.accepted();

        Companion newCompanion = Companion.builder()
                .travel(targetTravel)
                .userNumber(enrollment.getUserNumber())
                .build();
        companionRepository.save(newCompanion);

        //알림
        Notification newNotification = NotificationMaker.travelAcceptMessage(targetTravel, enrollment);
        newNotification = notificationRepository.save(newNotification);
        log.info("신청 수락 알림 생성: " + newNotification.toString());
    }

    @Transactional
    public void reject(long enrollmentNumber) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentNumber)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));
        Travel targetTravel = travelRepository.findByNumber(enrollment.getTravelNumber())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행 콘텐츠입니다."));
        authorizeTravelHost(targetTravel);
        
        enrollment.rejected();

        //알림
        Notification newNotification = NotificationMaker.travelRejectMessage(targetTravel, enrollment);
        newNotification = notificationRepository.save(newNotification);
        log.info("신청 거절 알림 생성: " + newNotification.toString());
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
