package swyp.swyp6_team7.travel.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelAppliedListResponseDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TravelAppliedServiceTest {

    @Mock
    private TravelRepository travelRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private CompanionRepository companionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private Tuple mockTuple;
    @InjectMocks
    private TravelAppliedService travelAppliedService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 신청한 여행 목록 조회")
    public void getAppliedTripsByUser_ShouldReturnListOfTrips() {
        // given
        Integer userNumber = 1;
        Travel travel = Travel.builder()
                .number(1)
                .title("Trip Title")
                .location("Seoul")
                .createdAt(LocalDateTime.now().minusDays(3))
                .dueDate(LocalDate.now().plusDays(10))
                .maxPerson(10)
                .status(TravelStatus.IN_PROGRESS)
                .build();

        Companion companion = Companion.builder()
                .travel(travel)
                .userNumber(userNumber)
                .build();

        Users user = Users.builder()
                .userNumber(userNumber)
                .userName("Test User")
                .build();

        // 모킹 설정: 사용자가 신청한 동반자 목록을 반환
        when(companionRepository.findByUserNumber(userNumber))
                .thenReturn(Collections.singletonList(companion));

        // 엔롤먼트 조회 모킹 설정 (Tuple)
        when(mockTuple.get(0, Long.class)).thenReturn(1L); // enrollment ID 또는 필요 값
        when(mockTuple.get(1, EnrollmentStatus.class)).thenReturn(EnrollmentStatus.ACCEPTED);
        when(enrollmentRepository.findEnrollmentsByUserNumber(userNumber))
                .thenReturn(Collections.singletonList(mockTuple));

        // 사용자를 찾기
        when(userRepository.findById(userNumber))
                .thenReturn(Optional.of(user));

        // 북마크 여부 확인
        when(bookmarkRepository.existsByUserNumberAndContentIdAndContentType(userNumber, travel.getNumber(), ContentType.TRAVEL))
                .thenReturn(true);

        // when
        List<TravelAppliedListResponseDto> result = travelAppliedService.getAppliedTripsByUser(userNumber);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Trip Title", result.get(0).getTitle());
        assertEquals("Seoul", result.get(0).getLocation());
        assertTrue(result.get(0).isBookmarked());

        verify(companionRepository, times(1)).findByUserNumber(userNumber);
        verify(enrollmentRepository, times(1)).findEnrollmentsByUserNumber(userNumber);
        verify(userRepository, times(1)).findById(userNumber);
        verify(bookmarkRepository, times(1)).existsByUserNumberAndContentIdAndContentType(userNumber, travel.getNumber(), ContentType.TRAVEL);
    }

    @Test
    @DisplayName("사용자가 특정 여행에 대한 참가 취소")
    public void cancelApplication_ShouldRemoveCompanion() {
        // given
        Integer userNumber = 1;
        int travelNumber = 1;
        Travel travel = Travel.builder()
                .number(travelNumber)
                .build();
        Companion companion = Companion.builder()
                .travel(travel)
                .userNumber(userNumber)
                .build();

        when(travelRepository.findById(travelNumber)).thenReturn(Optional.of(travel));
        when(companionRepository.findByTravelAndUserNumber(travel, userNumber)).thenReturn(Optional.of(companion));

        // when
        travelAppliedService.cancelApplication(userNumber, travelNumber);

        // then
        verify(companionRepository, times(1)).deleteByTravelAndUserNumber(travel, userNumber);
    }
}
