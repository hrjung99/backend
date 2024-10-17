package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TravelRequestedService {

    private final TravelRepository travelRepository;
    private final BookmarkRepository bookmarkRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<TravelListResponseDto> getRequestedTripsByUser(Integer userNumber, Pageable pageable) {
        // 사용자가 신청한 모든 여행 목록 조회
        List<Integer> travelNumbers = enrollmentRepository.findByUserNumber(userNumber).stream()
                .map(Enrollment::getTravelNumber) // 사용자가 신청한 여행의 travelNumber 가져오기
                .collect(Collectors.toList());

        List<Travel> requestedTravels = travelRepository.findAllById(travelNumbers).stream()
                .filter(travel -> travel.getStatus() != TravelStatus.DELETED) // 삭제된 여행 제외
                .collect(Collectors.toList());

        // TravelListResponseDto로 변환
        List<TravelListResponseDto> dtos = requestedTravels.stream()
                .map(travel -> {
                    Users host = userRepository.findByUserNumber(travel.getUserNumber())
                            .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));
                    int currentApplicants = travel.getCompanions().size();
                    boolean isBookmarked = bookmarkRepository.existsByUserNumberAndTravelNumber(userNumber, travel.getNumber());
                    return TravelListResponseDto.fromEntity(travel, host, currentApplicants, isBookmarked);
                })
                .collect(Collectors.toList());

        return toPage(dtos, pageable);
    }
    // 페이징 처리 메서드
    private Page<TravelListResponseDto> toPage(List<TravelListResponseDto> dtos, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());

        if (start > end || start > dtos.size()) {
            return new PageImpl<>(List.of(), pageable, dtos.size());
        }

        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }

    @Transactional
    public void cancelApplication(Integer userNumber, int travelNumber) {
        // 해당 여행 조회
        Travel travel = travelRepository.findById(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없습니다."));

        if (travel.getStatus() == TravelStatus.DELETED) {
            throw new IllegalArgumentException("삭제된 여행에 대한 신청은 취소할 수 없습니다.");
        }

        // 사용자의 신청 정보를 삭제
        Enrollment enrollment = enrollmentRepository.findOneByUserNumberAndTravelNumber(userNumber, travelNumber);
        if (enrollment == null) {
            throw new IllegalArgumentException("해당 여행에 대한 신청 정보를 찾을 수 없습니다.");
        }

        enrollmentRepository.delete(enrollment);
    }
}
