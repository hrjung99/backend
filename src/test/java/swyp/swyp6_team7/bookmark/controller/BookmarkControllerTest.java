package swyp.swyp6_team7.bookmark.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    @DisplayName("북마크 추가 테스트")
    @WithMockUser
    public void testAddBookmark() throws Exception {
        // Given
        BookmarkRequest request = new BookmarkRequest();
        request.setUserNumber(1);
        request.setContentId(100);

        // When
        doNothing().when(bookmarkService).addBookmark(any(BookmarkRequest.class));

        // Then
        mockMvc.perform(post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userNumber\": 1, \"itemId\": 100}"))
                .andExpect(status().isOk());

        verify(bookmarkService, times(1)).addBookmark(any(BookmarkRequest.class));
    }

    @Test
    @DisplayName("북마크 삭제 테스트")
    @WithMockUser
    public void testRemoveBookmark() throws Exception {
        // Given
        Integer bookmarkId = 1;

        // When
        doNothing().when(bookmarkService).removeBookmark(bookmarkId);

        // Then
        mockMvc.perform(delete("/api/bookmarks/{id}", bookmarkId))
                .andExpect(status().isNoContent());

        verify(bookmarkService, times(1)).removeBookmark(bookmarkId);
    }

    @Test
    @DisplayName("사용자 북마크 목록 조회 테스트")
    @WithMockUser
    public void testGetBookmarks() throws Exception {
        // Given
        String token = "Bearer validToken";
        Integer userNumber = 1;
        BookmarkResponse response1 = new BookmarkResponse(1, 100, "Content Type 1", LocalDateTime.now());
        BookmarkResponse response2 = new BookmarkResponse(2, 101, "Content Type 2", LocalDateTime.now());

        when(jwtProvider.getUserNumber(anyString())).thenReturn(userNumber);
        when(bookmarkService.getBookmarksByUser(userNumber)).thenReturn(List.of(response1, response2));

        // Then
        mockMvc.perform(get("/api/bookmarks")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].contentId").value(100))
                .andExpect(jsonPath("$[1].contentId").value(101))
                .andExpect(jsonPath("$[0].contentType").value("Content Type 1"))
                .andExpect(jsonPath("$[1].contentType").value("Content Type 2"));

        verify(jwtProvider, times(1)).getUserNumber(anyString());
        verify(bookmarkService, times(1)).getBookmarksByUser(userNumber);
    }
}

