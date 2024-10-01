package swyp.swyp6_team7.travel.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;
import swyp.swyp6_team7.enrollment.domain.QEnrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;
import swyp.swyp6_team7.enrollment.repository.EnrollmentCustomRepository;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelAppliedListResponseDto;
import swyp.swyp6_team7.travel.domain.QTravel;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;
import java.util.stream.Collectors;

import static swyp.swyp6_team7.travel.domain.QTravel.travel;

@Service
@RequiredArgsConstructor
public class TravelAppliedService {

    private final TravelRepository travelRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CompanionRepository companionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<TravelAppliedListResponseDto> getAppliedTripsByUser(Integer userNumber) {
        // 사용자가 승인된 동반자 목록 조회
        List<Companion> companions = companionRepository.findByUserNumber(userNumber);
        // 동반자 목록에서 수락된 상태의 엔롤먼트만 필터링
        List<Companion> acceptedCompanions = companions.stream()
                .filter(companion -> enrollmentRepository.findEnrollmentsByUserNumber(userNumber).stream()
                        .anyMatch(enrollment -> {
                            EnrollmentStatus status = enrollment.get(QEnrollment.enrollment.status);
                            return status == EnrollmentStatus.ACCEPTED;
                        }))
                .collect(Collectors.toList());

        // 여행 엔티티를 DTO로 변환하여 반환
        return companions.stream().map(companion -> {
            Travel travel = companion.getTravel();

            String dDay = TravelAppliedListResponseDto.formatDDay(travel.getDueDate()); // 디데이 형식으로 마감기한 포맷팅
            String postedAgo = TravelAppliedListResponseDto.formatPostedAgo(travel.getCreatedAt().toLocalDate()); // 작성일로부터 경과한 시간 포맷팅

            Users user = userRepository.findById(userNumber)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            // 동반자 수 계산
            int currentApplicants = travel.getCompanions().size();

            // 태그 리스트 추출
            List<String> tags = travel.getTravelTags().stream()
                    .map(travelTag -> travelTag.getTag().getName())
                    .collect(Collectors.toList());

            // 북마크 여부 확인
            boolean isBookmarked = bookmarkRepository.existsByUserNumberAndContentIdAndContentType(userNumber, travel.getNumber(), ContentType.TRAVEL);

            return new TravelAppliedListResponseDto(
                    travel.getNumber(),
                    travel.getTitle(),
                    travel.getLocation(),
                    user.getUserName(),
                    dDay,
                    postedAgo,
                    currentApplicants,
                    travel.getMaxPerson(),
                    travel.getStatus() == TravelStatus.CLOSED,
                    isBookmarked,
                    tags,
                    "/api/travel/" + travel.getNumber(),
                    "/api/my-applied-trips/" + travel.getNumber() + "/cancel",
                    "/api/bookmarks", // 북마크 추가 URL
                    "/api/bookmarks/" + travel.getNumber() // 북마크 제거 URL
            );
        }).collect(Collectors.toList());
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
