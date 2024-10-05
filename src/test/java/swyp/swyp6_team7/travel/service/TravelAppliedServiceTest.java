package swyp.swyp6_team7.travel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @InjectMocks
    private TravelAppliedService travelAppliedService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 신청한 여행 목록 조회")
    public void getAppliedTripsByUser(){
        // given
        Integer userNumber = 1;
        Pageable pageable = PageRequest.of(0, 5);
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();

        Travel travel = Travel.builder()
                .number(1)
                .title("Trip Title")
                .locationName("Seoul")
                .createdAt(LocalDateTime.now().minusDays(3))
                .dueDate(LocalDate.now().plusDays(10))
                .maxPerson(10)
                .status(TravelStatus.IN_PROGRESS)
                .location(travelLocation)
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

        // 사용자를 찾기
        when(userRepository.findById(userNumber))
                .thenReturn(Optional.of(user));

        // 북마크 여부 확인
        when(bookmarkRepository.existsByUserNumberAndTravelNumber(userNumber, travel.getNumber()))
                .thenReturn(true);
         // 여행 목록 페이지로 변환
        TravelListResponseDto travelListResponseDto = new TravelListResponseDto(
                travel.getNumber(),
                travel.getTitle(),
                travel.getLocationName(),
                travel.getUserNumber(),
                user.getUserName(),
                Collections.emptyList(), // 태그 리스트
                0,  //현재참여인원
                travel.getMaxPerson(),
                travel.getCreatedAt(),
                travel.getDueDate(),
                true // 북마크 여부
        );
        List<TravelListResponseDto> travelList = List.of(travelListResponseDto);
        Page<TravelListResponseDto> travelPage = new PageImpl<>(travelList, pageable, travelList.size());

        // when
        Page<TravelListResponseDto> result = travelAppliedService.getAppliedTripsByUser(userNumber, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Trip Title", result.getContent().get(0).getTitle());
        assertTrue(result.getContent().get(0).isBookmarked());

        verify(companionRepository, times(1)).findByUserNumber(userNumber);
        verify(userRepository, times(1)).findById(userNumber);
        verify(bookmarkRepository, times(1)).existsByUserNumberAndTravelNumber(userNumber, travel.getNumber());
    }


    @Test
    @DisplayName("사용자가 특정 여행에 대한 참가 취소")
    public void cancelApplication_ShouldRemoveCompanion() {
        // given
        Integer userNumber = 1;
        int travelNumber = 1;
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();
        Travel travel = Travel.builder()
                .number(travelNumber)
                .location(travelLocation)
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
