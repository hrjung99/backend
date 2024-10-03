package swyp.swyp6_team7.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TravelEnrollmentControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    TravelRepository travelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    CityRepository cityRepository;

    Travel travel;
    Users host;

    @BeforeEach
    void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        host = userRepository.save(Users.builder()
                .userEmail("host@test.com")
                .userPw("1234")
                .userName("host")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)
                .build()
        );
    }

    @BeforeEach
    void setTravel() {
        City city = City.builder()
                .cityName("제주")
                .cityType(CityType.DOMESTIC)
                .build();
        City savedCity = cityRepository.save(city);
        travel = travelRepository.save(Travel.builder()
                .title("기본 여행")
                .userNumber(host.getUserNumber())
                .maxPerson(3)
                .genderType(GenderType.NONE)
                .dueDate(LocalDate.now().plusDays(5))
                .periodType(PeriodType.NONE)
                .status(TravelStatus.IN_PROGRESS)
                        .city(savedCity)
                .build()
        );
    }


    @DisplayName("findElements: 주최자는 특정 여행에 대한 참가 신청서 목록을 조회할 수 있다")
    @Test
    public void findElementsWithHost() throws Exception {
        // given
        String url = "/api/travel/{travelNumber}/enrollments";
        Users user = userRepository.save(Users.builder()
                .userEmail("abc@test.com")
                .userPw("1234")
                .userName("username")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)
                .build());
        createEnrollment(user);

        var userDetails = userDetailsService.loadUserByUsername(host.getUserEmail());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        // when
        ResultActions resultActions = mockMvc.perform(get(url, travel.getNumber()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.enrollments[0].userName").value(user.getUserName()));
    }

    @DisplayName("findElements: 주최자가 아니면 특정 여행에 대한 참가 신청서 목록을 조회할 수 없다")
    @Test
    public void findElementsWithNotHost() throws Exception {
        // given
        String url = "/api/travel/{travelNumber}/enrollments";
        Users user = userRepository.save(Users.builder()
                .userEmail("abc@test.com")
                .userPw("1234")
                .userName("username")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)
                .build());
        createEnrollment(user);

        var userDetails = userDetailsService.loadUserByUsername(user.getUserEmail());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        // when
        ResultActions resultActions = mockMvc.perform(get(url, travel.getNumber()));

        // then
        resultActions
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("서버 에러: " + "여행 주최자의 권한이 필요한 작업입니다."));
    }


    private void createEnrollment(Users users) {
        enrollmentRepository.save(Enrollment.builder()
                .userNumber(users.getUserNumber())
                .travelNumber(travel.getNumber())
                .message("참가 신청합니다.")
                .status(EnrollmentStatus.PENDING)
                .build());
    }

}