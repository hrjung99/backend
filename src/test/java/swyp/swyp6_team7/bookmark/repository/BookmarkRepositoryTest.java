package swyp.swyp6_team7.bookmark.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.config.DataConfig;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DataConfig.class)
public class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    @DisplayName("사용자 번호로 북마크 조회 테스트")
    public void testFindByUserNumber() {
        // Given
        Bookmark bookmark1 = new Bookmark();
        bookmark1.setUserNumber(1);
        bookmark1.setContentId(101);
        bookmark1.setContentType(ContentType.TRAVEL);  // Enum 타입 사용
        bookmark1.setBookmarkDate(LocalDateTime.now());
        bookmarkRepository.save(bookmark1);

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setUserNumber(1);
        bookmark2.setContentId(102);
        bookmark2.setContentType(ContentType.COMMUNITY);  // Enum 타입 사용
        bookmark2.setBookmarkDate(LocalDateTime.now());
        bookmarkRepository.save(bookmark2);

        // When
        List<Bookmark> bookmarks = bookmarkRepository.findByUserNumber(1);

        // Then
        assertThat(bookmarks).hasSize(2);
        assertThat(bookmarks.get(0).getContentId()).isEqualTo(101);
        assertThat(bookmarks.get(0).getContentType()).isEqualTo(ContentType.TRAVEL);
        assertThat(bookmarks.get(1).getContentId()).isEqualTo(102);
        assertThat(bookmarks.get(1).getContentType()).isEqualTo(ContentType.COMMUNITY);
    }

    @Test
    @DisplayName("사용자 번호로 북마크 개수 조회 테스트")
    public void testCountByUserNumber() {
        // Given
        Bookmark bookmark = new Bookmark();
        bookmark.setUserNumber(1);
        bookmark.setContentId(101);
        bookmark.setContentType(ContentType.TRAVEL);  // Enum 타입 사용
        bookmark.setBookmarkDate(LocalDateTime.now());
        bookmarkRepository.save(bookmark);

        // When
        int count = bookmarkRepository.countByUserNumber(1);

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("가장 오래된 북마크 조회 테스트")
    public void testFindOldestByUserNumber() {
        // Given
        Bookmark bookmark1 = new Bookmark();
        bookmark1.setUserNumber(1);
        bookmark1.setContentId(101);
        bookmark1.setContentType(ContentType.TRAVEL);  // Enum 타입 사용
        bookmark1.setBookmarkDate(LocalDateTime.now().minusDays(2));
        bookmarkRepository.save(bookmark1);

        Bookmark bookmark2 = new Bookmark();
        bookmark2.setUserNumber(1);
        bookmark2.setContentId(102);
        bookmark2.setContentType(ContentType.COMMUNITY);  // Enum 타입 사용
        bookmark2.setBookmarkDate(LocalDateTime.now().minusDays(1));
        bookmarkRepository.save(bookmark2);

        // When
        List<Bookmark> oldestBookmarks = bookmarkRepository.findOldestByUserNumber(1);

        // Then
        assertThat(oldestBookmarks).hasSize(2);
        assertThat(oldestBookmarks.get(0).getContentId()).isEqualTo(101);
        assertThat(oldestBookmarks.get(0).getContentType()).isEqualTo(ContentType.TRAVEL);
        assertThat(oldestBookmarks.get(1).getContentId()).isEqualTo(102);
        assertThat(oldestBookmarks.get(1).getContentType()).isEqualTo(ContentType.COMMUNITY);
    }
}
