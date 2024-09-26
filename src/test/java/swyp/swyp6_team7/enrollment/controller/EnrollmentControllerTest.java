package swyp.swyp6_team7.enrollment.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swyp.swyp6_team7.enrollment.domain.Enrollment;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;
import swyp.swyp6_team7.enrollment.dto.EnrollmentCreateRequest;
import swyp.swyp6_team7.enrollment.repository.EnrollmentRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.GenderType;
import swyp.swyp6_team7.travel.domain.PeriodType;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    TravelRepository travelRepository;
    @Autowired
    UserRepository userRepository;

    Travel travel;
    Users user;

    @BeforeEach
    void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(Users.builder()
                .userNumber(1)
                .userEmail("abc@test.com")
                .userPw("1234")
                .userName("username")
                .userGender(Users.Gender.M)
                .userAgeGroup(Users.AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(Users.MemberStatus.ABLE)
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getUserPw(), user.getAuthorities()));
    }


    @DisplayName("create: 사용자는 여행 신청을 생성할 수 있다")
    @Test
    public void create() throws Exception {
        // given
        String url = "/api/enrollment";
        createTestTravel(2, LocalDate.now().plusDays(1), TravelStatus.IN_PROGRESS);
        EnrollmentCreateRequest request = EnrollmentCreateRequest.builder()
                .travelNumber(travel.getNumber())
                .message("여행 참가 희망합니다.")
                .build();

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(user.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().isCreated());

        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments.size()).isEqualTo(1);

        Enrollment created = enrollments.get(0);
        assertThat(created.getUserNumber()).isEqualTo(user.getUserNumber());
        assertThat(created.getTravelNumber()).isEqualTo(travel.getNumber());
        assertThat(created.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
    }

    @DisplayName("create: IN_PROGRESS 상태가 아닌 콘텐츠에는 신청할 수 없다")
    @Test
    public void createWhenTravelStatusNotInProgress() throws Exception {
        // given
        String url = "/api/enrollment";
        createTestTravel(2, LocalDate.now().plusDays(1), TravelStatus.CLOSED);
        EnrollmentCreateRequest request = EnrollmentCreateRequest.builder()
                .travelNumber(travel.getNumber())
                .message("여행 참가 희망합니다.")
                .build();

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(user.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("서버 에러: " + "참가 신청 할 수 없는 상태의 콘텐츠 입니다."));
    }

    @DisplayName("create: 마감 날짜가 지나간 콘텐츠에는 신청할 수 없다")
    @Test
    public void createWhenTravelDueDateIsOver() throws Exception {
        // given
        String url = "/api/enrollment";
        createTestTravel(2, LocalDate.now().minusDays(1), TravelStatus.IN_PROGRESS);
        EnrollmentCreateRequest request = EnrollmentCreateRequest.builder()
                .travelNumber(travel.getNumber())
                .message("여행 참가 희망합니다.")
                .build();

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(user.getEmail());

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("서버 에러: " + "참가 신청 할 수 없는 상태의 콘텐츠 입니다."));
    }


    private void createTestTravel(int maxPerson, LocalDate dueDate, TravelStatus status) {
        Users host = userRepository.save(Users.builder()
                .userEmail("host@test.com")
                .userPw("1234")
                .userName("host")
                .userGender(Users.Gender.M)
                .userAgeGroup(Users.AgeGroup.TEEN)
                .userRegDate(LocalDateTime.now())
                .userStatus(Users.MemberStatus.ABLE)
                .build()
        );
        travel = travelRepository.save(Travel.builder()
                .title("기본 여행")
                .userNumber(host.getUserNumber())
                .maxPerson(maxPerson)
                .genderType(GenderType.NONE)
                .dueDate(dueDate)
                .periodType(PeriodType.NONE)
                .status(status)
                .build()
        );
    }

}