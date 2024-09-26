package swyp.swyp6_team7.bookmark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.bookmark.service.BookmarkService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BookmarkServiceTest {
    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    public void addBookmarkTest() {
        BookmarkRequest request = new BookmarkRequest();
        request.setUserNumber(1);
        request.setContentId(100);
        request.setContentType("TRAVEL");

        // 북마크 추가
        bookmarkService.addBookmark(request);

        // 북마크가 정상적으로 추가되었는지 확인
        assertThat(bookmarkRepository.countByUserNumber(1)).isGreaterThan(0);
    }

    @Test
    public void addBookmarkLimitTest() {
        BookmarkRequest request = new BookmarkRequest();
        request.setUserNumber(1);
        request.setContentId(101);
        request.setContentType("TRAVEL");

        // 북마크 제한 초과 시 예외 발생 확인
        for (int i = 0; i < 30; i++) {
            bookmarkService.addBookmark(request);
        }

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bookmarkService.addBookmark(request);
        });

        assertThat(exception.getMessage()).isEqualTo("최대 30개의 북마크만 저장할 수 있습니다.");
    }

    @Test
    public void removeBookmarkTest() {
        BookmarkRequest request = new BookmarkRequest();
        request.setUserNumber(1);
        request.setContentId(102);
        request.setContentType("TRAVEL");

        // 북마크 추가 후 삭제
        bookmarkService.addBookmark(request);
        bookmarkService.removeBookmark(request.getContentId());

        // 북마크가 정상적으로 삭제되었는지 확인
        assertThat(bookmarkRepository.countByUserNumber(1)).isEqualTo(0);
    }
}
