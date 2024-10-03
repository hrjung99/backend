package swyp.swyp6_team7.companion.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.companion.dto.CompanionInfoDto;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.location.domain.City;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.repository.CityRepository;
import swyp.swyp6_team7.member.entity.AgeGroup;
import swyp.swyp6_team7.member.entity.Gender;
import swyp.swyp6_team7.member.entity.UserStatus;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CompanionServiceTest {

    @Autowired
    private CompanionService companionService;
    @Autowired
    private CompanionRepository companionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private CityRepository cityRepository;

    Travel targetTravel;

    @BeforeEach
    void setUp() {
        companionRepository.deleteAll();
        userRepository.deleteAll();
        travelRepository.deleteAll();
        City city = new City();
        city.setCityName("제주");
        city.setCityType(CityType.DOMESTIC);
        City savedCity = cityRepository.save(city);

        targetTravel = travelRepository.save(Travel.builder()
                .title("Travel Controller")
                .location("제주")
                .city(savedCity)
                .userNumber(100)
                .genderType(GenderType.NONE)
                .periodType(PeriodType.NONE)
                .status(TravelStatus.IN_PROGRESS)
                .build());
    }


    @DisplayName("findCompanionInfo: 특정 여행의 companion 정보를 가져올 수 있다")
    @Test
    public void findCompanionInfoByTravelNumber() {
        // given
        Users users = userRepository.save(Users.builder()
                .userEmail("abc@test.com")
                .userPw("1234")
                .userName("username")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)
                .build());
        Companion newCompanion = companionRepository.save(Companion.builder()
                .userNumber(users.getUserNumber())
                .travel(targetTravel)
                .build());

        // when
        List<CompanionInfoDto> companions = companionService
                .findCompanionsByTravelNumber(targetTravel.getNumber());

        // then
        assertThat(companions.size()).isEqualTo(1);
        assertThat(companions.get(0).getUserNumber()).isEqualTo(newCompanion.getUserNumber());
        assertThat(companions.get(0).getUserName()).isEqualTo(users.getUserName());
    }

}