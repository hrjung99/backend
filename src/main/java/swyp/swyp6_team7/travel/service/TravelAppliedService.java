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
import swyp.swyp6_team7.enrollment.domain.QEnrollment;
import swyp.swyp6_team7.enrollment.dto.EnrollmentResponse;
import swyp.swyp6_team7.enrollment.repository.EnrollmentCustomRepository;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelAppliedListResponseDto;
import swyp.swyp6_team7.travel.domain.QTravel;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelAppliedService {

    private final TravelRepository travelRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CompanionRepository companionRepository;
    private final EnrollmentRepository enrollmentRepository;


    QTravel travel = QTravel.travel;
    QEnrollment enrollment = QEnrollment.enrollment;

    @Transactional(readOnly = true)
    public List<TravelAppliedListResponseDto> getAppliedTripsByUser(Integer userNumber) {
        // 사용자가 신청한 여행 엔티티를 Enrollment를 통해 조회
        List<Tuple> enrollments = enrollmentRepository.findEnrollmentsByUserNumber(userNumber);

        // 여행 엔티티를 DTO로 변환하여 반환
        return enrollments.stream().map(tuple -> {
            Long enrollmentNumber = tuple.get(0, Long.class); // 신청 번호
            Integer travelNumber = tuple.get(1, Integer.class); // 여행 번호

            Travel travel = travelRepository.findById(travelNumber)
                    .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없습니다."));

            String dDay = TravelAppliedListResponseDto.formatDDay(travel.getDueDate()); // 디데이 형식으로 마감기한 포맷팅
            String postedAgo = TravelAppliedListResponseDto.formatPostedAgo(travel.getCreatedAt().toLocalDate()); // 작성일로부터 경과한 시간 포맷팅

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
                    "사용자 이름", // 사용자 이름은 Enrollment에 별도로 존재하지 않으므로 필요 시 추가적으로 조회해야 함.
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

    @Transactional
    public void addBookmark(Integer userNumber, int travelNumber) {
        if (!bookmarkRepository.existsByUserNumberAndContentIdAndContentType(userNumber, travelNumber, ContentType.TRAVEL)) {
            bookmarkRepository.save(new Bookmark(userNumber, travelNumber, ContentType.TRAVEL));
        }
    }

    @Transactional
    public void removeBookmark(Integer userNumber, int travelNumber) {
        bookmarkRepository.deleteByUserNumberAndContentIdAndContentType(userNumber, travelNumber, ContentType.TRAVEL);
    }
}
