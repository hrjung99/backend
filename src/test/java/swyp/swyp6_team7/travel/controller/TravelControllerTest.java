package swyp.swyp6_team7.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.repository.LocationRepository;
import swyp.swyp6_team7.member.entity.*;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TravelControllerTest {

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
    LocationRepository locationRepository;

    Users user;

    @BeforeEach
    void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        travelRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(Users.builder()
                .userNumber(1)
                .userEmail("abc@test.com")
                .userPw("1234")
                .userName("username")
                .userGender(Gender.M)
                .userAgeGroup(AgeGroup.TWENTY)
                .role(UserRole.USER)
                .userRegDate(LocalDateTime.now())
                .userStatus(UserStatus.ABLE)
                .build());

        var userDetails = userDetailsService.loadUserByUsername(user.getUserEmail());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

//        SecurityContext context = SecurityContextHolder.getContext();
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(user, user.getUserPw(), user.getAuthorities());
//
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getUserPw(), user.getAuthorities()));
    }

    @DisplayName("create: 사용자는 여행 콘텐츠를 생성할 수 있다")
    @Test
    public void create() throws Exception {
        // given
        locationRepository.deleteAll();
        travelRepository.deleteAll();
        String url = "/api/travel";
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();
        Location savedLocation = locationRepository.save(travelLocation);
        TravelCreateRequest request = TravelCreateRequest.builder()
                .title("Controller create")
                .completionStatus(true)
                .location(savedLocation.getLocationName())
                .build();


        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(user.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andExpect(status().isCreated());
        List<Travel> travels = travelRepository.findAll();
        assertThat(travels.size()).isEqualTo(1);
        assertThat(travels.get(0).getTitle()).isEqualTo("Controller create");
        assertThat(travels.get(0).getUserNumber()).isEqualTo(user.getUserNumber());
    }


    @DisplayName("getDetailsByNumber: 여행 콘텐츠 단건 상세 정보 조회에 성공한다")
    @Test
    public void getDetailsByNumber() throws Exception {
        // given
        String url = "/api/travel/detail/{travelNumber}";
        Travel savedTravel = createTravel(user.getUserNumber(), TravelStatus.IN_PROGRESS);
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();
        Location savedLocation = locationRepository.save(travelLocation);

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber(), savedLocation));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(savedTravel.getTitle()))
                .andExpect(jsonPath("$.userNumber").value(user.getUserNumber()));
    }

    @DisplayName("getDetailsByNumber: 작성자가 아닌 경우 Draft 상태의 콘텐츠 단건 조회를 하면 예외가 발생")
    @Test
    public void getDetailsByNumberDraftException() throws Exception {
        // given
        String url = "/api/travel/detail/{travelNumber}";
        Travel savedTravel = createTravel(2, TravelStatus.DRAFT);
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber(), travelLocation));

        // then
        resultActions
                .andExpect(status().is5xxServerError());
    }

    @DisplayName("getDetailsByNumber: Deleted 상태의 콘텐츠 단건 조회를 하면 예외가 발생")
    @Test
    public void getDetailsByNumberDeletedException() throws Exception {
        // given
        String url = "/api/travel/detail/{travelNumber}";
        Travel savedTravel = createTravel(user.getUserNumber(), TravelStatus.DELETED);
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber(), travelLocation));

        // then
        resultActions
                .andExpect(status().is5xxServerError());
    }

    private Travel createTravel(int userNumber, TravelStatus status) {
        Location travelLocation = Location.builder()
                .locationName("Seoul")
                .locationType(LocationType.DOMESTIC)
                .build();
        Location savedLocation = locationRepository.save(travelLocation);
        return travelRepository.save(Travel.builder()
                .title("Travel Controller")
                        .travelLocation(travelLocation)
                .userNumber(userNumber)
                .genderType(GenderType.NONE)
                .periodType(PeriodType.NONE)
                .status(status)
                .build()
        );
    }
}