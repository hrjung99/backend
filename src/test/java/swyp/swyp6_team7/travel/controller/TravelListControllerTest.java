package swyp.swyp6_team7.travel.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.service.TravelListService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelListService travelListService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("내가 만든 여행 게시글 목록 조회 테스트")
    @WithMockUser // Mock User로 인증된 상태를 시뮬레이션합니다.
    public void testGetMyCreatedTravels() throws Exception {
        // Given
        String token = "Bearer validToken";
        Integer userNumber = 1;

        // Mock JWT 프로바이더로 사용자 번호를 추출합니다.
        when(jwtProvider.getUserNumber(any(String.class))).thenReturn(userNumber);

        // 여행 목록을 임의로 설정합니다.
        TravelListResponseDto travel1 = new TravelListResponseDto(
                1, "Title 1", "Location 1", "Username 1", "마감 D-3", "2일 전",
                2, 5, false, List.of("Tag1", "Tag2")
        );
        TravelListResponseDto travel2 = new TravelListResponseDto(
                2, "Title 2", "Location 2", "Username 2", "종료됨", "6일 전",
                3, 10, true, List.of("Tag3", "Tag4")
        );
        List<TravelListResponseDto> travelList = List.of(travel1, travel2);

        // When
        when(travelListService.getTravelListByUser(userNumber)).thenReturn(travelList);

        // Then
        mockMvc.perform(get("/api/my-travels")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[0].location").value("Location 1"))
                .andExpect(jsonPath("$[0].username").value("Username 1"))
                .andExpect(jsonPath("$[0].dday").value("마감 D-3"))
                .andExpect(jsonPath("$[0].postedAgo").value("2일 전"))
                .andExpect(jsonPath("$[0].currentApplicants").value(2))
                .andExpect(jsonPath("$[0].maxPerson").value(5))
                .andExpect(jsonPath("$[0].completionStatus").value(false))
                .andExpect(jsonPath("$[0].tags.length()").value(2))
                .andExpect(jsonPath("$[0].tags[0]").value("Tag1"))
                .andExpect(jsonPath("$[0].tags[1]").value("Tag2"))
                .andExpect(jsonPath("$[1].title").value("Title 2"))
                .andExpect(jsonPath("$[1].location").value("Location 2"))
                .andExpect(jsonPath("$[1].username").value("Username 2"))
                .andExpect(jsonPath("$[1].dday").value("종료됨"))
                .andExpect(jsonPath("$[1].postedAgo").value("6일 전"))
                .andExpect(jsonPath("$[1].currentApplicants").value(3))
                .andExpect(jsonPath("$[1].maxPerson").value(10))
                .andExpect(jsonPath("$[1].completionStatus").value(true))
                .andExpect(jsonPath("$[1].tags.length()").value(2))
                .andExpect(jsonPath("$[1].tags[0]").value("Tag3"))
                .andExpect(jsonPath("$[1].tags[1]").value("Tag4"));
    }
}
