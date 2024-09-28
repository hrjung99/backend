package swyp.swyp6_team7.bookmark.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.dto.BookmarkResponse;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @InjectMocks
    private BookmarkService bookmarkService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("북마크 추가 - 북마크 개수 초과 시 오래된 북마크 삭제")
    public void testAddBookmark_MaxLimit() {
        // Given
        BookmarkRequest request = new BookmarkRequest(1, 101, "TRAVEL");
        Bookmark oldestBookmark = new Bookmark(1, 1, 100, ContentType.TRAVEL, LocalDateTime.now().minusDays(10));
        List<Bookmark> oldestBookmarks = List.of(oldestBookmark);

        when(bookmarkRepository.countByUserNumber(1)).thenReturn(30);
        when(bookmarkRepository.findOldestByUserNumber(1)).thenReturn(oldestBookmarks);

        // When
        bookmarkService.addBookmark(request);

        // Then
        verify(bookmarkRepository, times(1)).delete(oldestBookmark);
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 추가 - 정상 추가")
    public void testAddBookmark_Normal() {
        // Given
        BookmarkRequest request = new BookmarkRequest(1, 101, "TRAVEL");

        when(bookmarkRepository.countByUserNumber(1)).thenReturn(29);

        // When
        bookmarkService.addBookmark(request);

        // Then
        verify(bookmarkRepository, never()).delete(any());
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
    }

    @Test
    @DisplayName("북마크 삭제")
    public void testRemoveBookmark() {
        // Given
        Integer bookmarkId = 1;

        doNothing().when(bookmarkRepository).deleteById(bookmarkId);

        // When
        bookmarkService.removeBookmark(bookmarkId);

        // Then
        verify(bookmarkRepository, times(1)).deleteById(bookmarkId);
    }

    @Test
    @DisplayName("사용자의 북마크 목록 조회 - 빈 목록 처리")
    public void testGetBookmarksByUser_EmptyList() {
        // Given
        Integer userNumber = 1;

        when(bookmarkRepository.findByUserNumber(userNumber)).thenReturn(List.of());

        // When
        List<BookmarkResponse> response = bookmarkService.getBookmarksByUser(userNumber);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("사용자의 북마크 목록 조회 - 정렬된 결과 반환")
    public void testGetBookmarksByUser_SortedList() {
        // Given
        Integer userNumber = 1;

        Bookmark travelBookmark1 = new Bookmark(1, userNumber, 101, ContentType.TRAVEL, LocalDateTime.now().minusDays(3));
        Bookmark travelBookmark2 = new Bookmark(2, userNumber, 102, ContentType.TRAVEL, LocalDateTime.now().minusDays(1));
        Bookmark communityBookmark = new Bookmark(3, userNumber, 103, ContentType.COMMUNITY, LocalDateTime.now());

        List<Bookmark> bookmarks = List.of(travelBookmark1, travelBookmark2, communityBookmark);

        when(bookmarkRepository.findByUserNumber(userNumber)).thenReturn(bookmarks);

        // When
        List<BookmarkResponse> response = bookmarkService.getBookmarksByUser(userNumber);

        // Then
        assertThat(response).hasSize(3);
        assertThat(response.get(0).getContentId()).isEqualTo(travelBookmark1.getContentId());
        assertThat(response.get(1).getContentId()).isEqualTo(travelBookmark2.getContentId());
        assertThat(response.get(2).getContentId()).isEqualTo(communityBookmark.getContentId());
    }
}
