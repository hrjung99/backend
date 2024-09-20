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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    TravelRepository travelRepository;
    @Autowired
    UserRepository userRepository;

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
                .userEmail("test@naver.com")
                .userPw("1234")
                .userName("username")
                .userGender(Users.Gender.M)
                .userBirthYear("2000")
                .userPhone("01012345678")
                .userRole("user")
                .userRegDate(LocalDateTime.now())
                .userStatus(Users.MemberStatus.ABLE)
                .build());
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        user.setRoles(roles);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getUserPw(), user.getAuthorities()));
    }


    @DisplayName("getDetailsByNumber: 여행 콘텐츠 단건 상세 정보 조회에 성공한다")
    @Test
    public void getDetailsByNumber() throws Exception {
        // given
        String url = "/api/travel/detail/{travelNumber}";
        Travel savedTravel = createTravel(user.getUserNumber(), TravelStatus.IN_PROGRESS);

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(savedTravel.getTitle()))
                .andExpect(jsonPath("$.userNumber").value(user.getUserNumber()))
                .andExpect(jsonPath("$.userName").value(user.getUserName()));
    }

    @DisplayName("getDetailsByNumber: 작성자가 아닌 경우 Draft 상태의 콘텐츠 단건 조회를 하면 예외가 발생")
    @Test
    public void getDetailsByNumberDraftException() throws Exception {
        // given
        String url = "/api/travel/detail/{travelNumber}";
        Travel savedTravel = createTravel(2, TravelStatus.DRAFT);

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber()));

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

        // when
        ResultActions resultActions = mockMvc.perform(get(url, savedTravel.getNumber()));

        // then
        resultActions
                .andExpect(status().is5xxServerError());
    }

    private Travel createTravel(int userNumber, TravelStatus status) {
        return travelRepository.save(Travel.builder()
                .title("Travel Controller")
                .userNumber(userNumber)
                .status(status)
                .build()
        );
    }
}