package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
@Service
@RequiredArgsConstructor
public class TravelAppliedService {

    private final TravelRepository travelRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CompanionRepository companionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public Page<TravelListResponseDto> getAppliedTripsByUser(Integer userNumber, Pageable pageable) {
        // 사용자가 승인된 동반자 목록 조회
        List<Companion> companions = companionRepository.findByUserNumber(userNumber);

        List<TravelListResponseDto> dtos = companions.stream().map(companion -> {
            Travel travel = companion.getTravel();
            Users user = userRepository.findById(userNumber)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            int currentApplicants = travel.getCompanions().size();

            boolean isBookmarked = bookmarkRepository.existsByUserNumberAndTravelNumber(userNumber, travel.getNumber());

            return TravelListResponseDto.fromEntity(travel, user, currentApplicants, isBookmarked);
        }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtos.size());
        return new PageImpl<>(dtos.subList(start, end), pageable, dtos.size());
    }


    @Transactional
    public void cancelApplication(Integer userNumber, int travelNumber) {
        Travel travel = travelRepository.findById(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없습니다."));

        // 사용자가 신청자인지 확인하고 신청 정보 삭제
        Companion companion = companionRepository.findByTravelAndUserNumber(travel, userNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 여행에 대한 사용자의 신청 정보를 찾을 수 없습니다."));

        companionRepository.deleteByTravelAndUserNumber(travel, userNumber);
    }
}
