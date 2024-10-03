package swyp.swyp6_team7.travel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.repository.LocationRepository;
import swyp.swyp6_team7.member.entity.AgeGroup;
import swyp.swyp6_team7.member.entity.Gender;
import swyp.swyp6_team7.member.entity.UserStatus;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TravelServiceTest {

    @Autowired
    private TravelService travelService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TravelRepository travelRepository;
    
    Users user;

    @BeforeEach
    void setUp() {
        travelRepository.deleteAll();
        locationRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(Users.builder()
                .userEmail("test@naver.com")
                .userPw("1234")
                .userName("username")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TWENTY)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)

                .build());

    }

    @DisplayName("create: 이메일로 유저를 가져와 여행 콘텐츠를 만들 수 있다")
    @Test
    public void createTravelWithUser() {
        // given
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();
        Location savedLocation = locationRepository.save(travelLocation);
        TravelCreateRequest request = TravelCreateRequest.builder()
                .title("test travel post")
                .completionStatus(true)
                .location(savedLocation.getLocationName())
                .build();

        // when
        Travel createdTravel = travelService.create(request, "test@naver.com");

        // then
        assertThat(createdTravel.getTitle()).isEqualTo(request.getTitle());
        assertThat(createdTravel.getUserNumber()).isEqualTo(user.getUserNumber());
        assertThat(createdTravel.getLocation()).isEqualTo(savedLocation.getLocationName());
    }

}