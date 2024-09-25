package swyp.swyp6_team7.enrollment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentCreateRequest;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;

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

}
