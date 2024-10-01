package swyp.swyp6_team7.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.travel.dto.response.TravelAppliedListResponseDto;
import swyp.swyp6_team7.travel.service.TravelAppliedService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelAppliedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelAppliedService travelAppliedService;

    @MockBean
    private JwtProvider jwtProvider;

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_TOKEN = "Bearer test-token";

    @DisplayName("사용자가 신청한 여행 목록을 조회한다")
    @WithMockUser
    @Test
    void getAppliedTrips_ShouldReturnListOfAppliedTrips() throws Exception {
        // given
        int userNumber = 1;
        List<TravelAppliedListResponseDto> mockAppliedTrips = List.of(
                new TravelAppliedListResponseDto(
                        2,
                        "강릉 갈사람",
                        "강릉",
                        "username",
                        "마감 D-28",
                        "오늘",
                        0,
                        2,
                        false,
                        true,
                        List.of("가성비", "핫플"),
                        "/api/travel/2",
                        "/api/my-applied-travels/2/cancel",
                        "/api/bookmarks",
                        "/api/bookmarks/2"
                )
        );

        // Mock the JwtProvider to return the userNumber from the token
        when(jwtProvider.getUserNumber("test-token")).thenReturn(userNumber);

        // Mock the TravelAppliedService to return a list of applied travels
        when(travelAppliedService.getAppliedTripsByUser(userNumber)).thenReturn(mockAppliedTrips);

        // when & then
        mockMvc.perform(get("/api/my-applied-travels")
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].travelNumber").value(2))
                .andExpect(jsonPath("$[0].title").value("강릉 갈사람"))
                .andExpect(jsonPath("$[0].location").value("강릉"))
                .andExpect(jsonPath("$[0].username").value("username"))
                .andExpect(jsonPath("$[0].dday").value("마감 D-28"))
                .andExpect(jsonPath("$[0].postedAgo").value("오늘"))
                .andExpect(jsonPath("$[0].currentApplicants").value(0))
                .andExpect(jsonPath("$[0].maxPerson").value(2))
                .andExpect(jsonPath("$[0].completionStatus").value(false))
                .andExpect(jsonPath("$[0].bookmarked").value(true))
                .andExpect(jsonPath("$[0].tags[0]").value("가성비"))
                .andExpect(jsonPath("$[0].tags[1]").value("핫플"))
                .andExpect(jsonPath("$[0].detailUrl").value("/api/travel/2"))
                .andExpect(jsonPath("$[0].cancelApplicationUrl").value("/api/my-applied-travels/2/cancel"))
                .andExpect(jsonPath("$[0].addBookmarkUrl").value("/api/bookmarks"))
                .andExpect(jsonPath("$[0].removeBookmarkUrl").value("/api/bookmarks/2"));
    }

    @DisplayName("사용자가 특정 여행에 대한 참가 신청을 취소한다")
    @WithMockUser
    @Test
    void cancelTripApplication_ShouldCancelApplication() throws Exception {
        // given
        int userNumber = 1;
        int travelNumber = 2;

        // Mock the JwtProvider to return the userNumber from the token
        when(jwtProvider.getUserNumber("test-token")).thenReturn(userNumber);

        // Do nothing when canceling the application
        Mockito.doNothing().when(travelAppliedService).cancelApplication(userNumber, travelNumber);

        // when & then
        mockMvc.perform(delete("/api/my-applied-travels/{travelNumber}/cancel", travelNumber)
                        .header(AUTHORIZATION_HEADER, BEARER_TOKEN))
                .andExpect(status().isNoContent());
    }
}
