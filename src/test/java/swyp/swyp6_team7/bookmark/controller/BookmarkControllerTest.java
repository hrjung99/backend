package swyp.swyp6_team7.bookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.dto.BookmarkResponse;
import swyp.swyp6_team7.bookmark.service.BookmarkService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private JwtProvider jwtProvider;
    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    @BeforeEach
    void setUp() {
        jwtToken = "Bearer test-token";
        Mockito.when(jwtProvider.getUserNumber(any())).thenReturn(1);
    }

    @Test
    @DisplayName("북마크 추가 테스트")
    @WithMockUser
    public void testAddBookmark() throws Exception {
        BookmarkRequest request = new BookmarkRequest();
        request.setTravelNumber(1);

        mockMvc.perform(post("/api/bookmarks")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(bookmarkService).addBookmark(any(BookmarkRequest.class));
    }

    @Test
    @DisplayName("북마크 삭제 테스트")
    @WithMockUser
    public void testRemoveBookmark() throws Exception {
        // Given
        int travelNumber = 1;

        mockMvc.perform(delete("/api/bookmarks/{travelNumber}", travelNumber)
                        .header("Authorization", jwtToken))
                .andExpect(status().isNoContent());

        Mockito.verify(bookmarkService).removeBookmark(travelNumber, 1);
    }

    @Test
    @DisplayName("사용자 북마크 목록 조회 테스트")
    @WithMockUser
    void testGetBookmarks() throws Exception {
        BookmarkResponse response = new BookmarkResponse(
                1,
                "제목",
                1,
                "작성자",
                List.of("가성비", "핫플"),
                1,
                4,
                "2024년 09월 21일",
                "2025년 05월 15일",
                true);

        List<BookmarkResponse> responses = List.of(response);
        PageRequest pageable = PageRequest.of(0, 5);
        Page<BookmarkResponse> pageResponse = new PageImpl<>(responses, pageable, responses.size());

        // When
        when(bookmarkService.getBookmarksByUser(anyInt(), anyInt(), anyInt())).thenReturn(pageResponse);

        // Then
        mockMvc.perform(get("/api/bookmarks")
                        .header("Authorization", jwtToken)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].travelNumber").value(1))
                .andExpect(jsonPath("$.content[0].title").value("제목"))
                .andExpect(jsonPath("$.content[0].userName").value("작성자"))
                //.andExpect(jsonPath("$.content[0].isBookmarked").value(true))
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(1))
                .andExpect(jsonPath("$.page.totalPages").value(1));

        verify(bookmarkService).getBookmarksByUser(1, 0, 5);
    }
    @Test
    @WithMockUser
    @DisplayName("사용자의 북마크된 여행 번호 목록 조회 테스트")
    public void getBookmarkedTravelNumbers_ShouldReturnListOfTravelNumbers() throws Exception {
        // Given
        String token = "Bearer test-token";
        Integer userNumber = 1;
        List<Integer> travelNumbers = List.of(101, 102, 103);

        // When
        when(jwtProvider.getUserNumber("test-token")).thenReturn(userNumber);
        when(bookmarkService.getBookmarkedTravelNumbers(userNumber)).thenReturn(travelNumbers);

        // Then
        mockMvc.perform(get("/api/bookmarks/travel-number")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(101))
                .andExpect(jsonPath("$[1]").value(102))
                .andExpect(jsonPath("$[2]").value(103));
    }

}

