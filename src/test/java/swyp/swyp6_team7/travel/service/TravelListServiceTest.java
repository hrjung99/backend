package swyp.swyp6_team7.travel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.travel.service.TravelListService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TravelListServiceTest {

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;  // 추가된 부분

    @InjectMocks
    private TravelListService travelListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 작성한 여행 목록 조회 테스트")
    void testGetTravelListByUser() {
        // Given
        Integer userNumber = 1;

        Travel travel1 = Travel.builder()
                .number(1)
                .userNumber(userNumber)
                .createdAt(LocalDateTime.now().minusDays(2))
                .location("Location 1")
                .title("Title 1")
                .details("Details 1")
                .viewCount(0)
                .maxPerson(5)
                .genderType(GenderType.MAN_ONLY)
                .dueDate(LocalDate.now().plusDays(3))
                .periodType(PeriodType.ONE_WEEK)
                .status(TravelStatus.IN_PROGRESS)
                .build();

        Travel travel2 = Travel.builder()
                .number(2)
                .userNumber(userNumber)
                .createdAt(LocalDateTime.now().minusDays(6))
                .location("Location 2")
                .title("Title 2")
                .details("Details 2")
                .viewCount(0)
                .maxPerson(10)
                .genderType(GenderType.WOMAN_ONLY)
                .dueDate(LocalDate.now().minusDays(1))
                .periodType(PeriodType.MORE_THAN_MONTH)
                .status(TravelStatus.CLOSED)
                .build();

        // 태그 설정
        Tag tag1 = Tag.of("tag1");
        Tag tag2 = Tag.of("tag2");

        TravelTag travelTag1 = new TravelTag(null, travel1, tag1);
        TravelTag travelTag2 = new TravelTag(null, travel2, tag2);

        travel1.getTravelTags().add(travelTag1);
        travel2.getTravelTags().add(travelTag2);

        List<Travel> travels = Arrays.asList(travel1, travel2);

        // 사용자 설정
        Users user = Users.builder().userNumber(userNumber).userName("Username 1").build();

        // When
        when(travelRepository.findByUserNumber(userNumber)).thenReturn(travels);
        when(userRepository.findByUserNumber(userNumber)).thenReturn(Optional.of(user));
        when(bookmarkRepository.existsByUserNumberAndTravelNumber(eq(userNumber), anyInt())).thenReturn(true);

        // Then
        List<TravelListResponseDto> result = travelListService.getTravelListByUser(userNumber);

        assertEquals(2, result.size());

        TravelListResponseDto travel1Dto = result.get(0);
        assertEquals("Title 1", travel1Dto.getTitle());
        assertEquals("Location 1", travel1Dto.getLocation());
        assertEquals("Username 1", travel1Dto.getUsername());
        assertEquals("마감 D-3", travel1Dto.getDDay());
        assertEquals("2일 전", travel1Dto.getPostedAgo());
        assertEquals(0, travel1Dto.getCurrentApplicants());
        assertEquals(5, travel1Dto.getMaxPerson());
        assertEquals(false, travel1Dto.isCompletionStatus());
        assertEquals(List.of("tag1"), travel1Dto.getTags());

        TravelListResponseDto travel2Dto = result.get(1);
        assertEquals("Title 2", travel2Dto.getTitle());
        assertEquals("Location 2", travel2Dto.getLocation());
        assertEquals("Username 1", travel2Dto.getUsername());
        assertEquals("종료됨", travel2Dto.getDDay());
        assertEquals("6일 전", travel2Dto.getPostedAgo());
        assertEquals(0, travel2Dto.getCurrentApplicants());
        assertEquals(10, travel2Dto.getMaxPerson());
        assertEquals(true, travel2Dto.isCompletionStatus());
        assertEquals(List.of("tag2"), travel2Dto.getTags());

        verify(travelRepository, times(1)).findByUserNumber(userNumber);
        //verify(userRepository, times(1)).findByUserNumber(userNumber);
        verify(bookmarkRepository, times(2)).existsByUserNumberAndTravelNumber(eq(userNumber), anyInt());
    }
}
