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
        BookmarkResponse response = new BookmarkResponse(1, true, 1,"제목", "위치", "작성자", "오늘", "마감 D-5", 1, 4, false, List.of("가성비", "핫플"), "/api/travel/1", "/api/bookmarks/1");
        List<BookmarkResponse> responses = List.of(response);

        Mockito.when(bookmarkService.getBookmarksByUser(anyInt())).thenReturn(responses);

        mockMvc.perform(get("/api/bookmarks")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].travelNumber").value(1))
                .andExpect(jsonPath("$[0].title").value("제목"))
                .andExpect(jsonPath("$[0].location").value("위치"))
                .andExpect(jsonPath("$[0].username").value("작성자"))
                .andExpect(jsonPath("$[0].bookmarked").value(true));

        Mockito.verify(bookmarkService).getBookmarksByUser(1);
    }
}

