package swyp.swyp6_team7.bookmark.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.dto.BookmarkResponse;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Transactional
    public void addBookmark(BookmarkRequest request) {
        // 북마크 개수 제한 확인
        if (bookmarkRepository.countByUserNumber(request.getUserNumber()) >= 30) {
            throw new IllegalStateException("최대 30개의 북마크만 저장할 수 있습니다.");
        }

        // 북마크 저장
        Bookmark bookmark = new Bookmark(
                request.getUserNumber(),
                request.getContentId(),
                ContentType.valueOf(request.getContentType())
        );
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Integer bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponse> getBookmarksByUser(Integer userNumber) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserNumber(userNumber);
        return bookmarks.stream()
                .map(bookmark -> new BookmarkResponse(
                        bookmark.getBookmarkId(),
                        bookmark.getContentId(),
                        bookmark.getContentType().name(),
                        bookmark.getBookmarkDate()
                ))
                .collect(Collectors.toList());
    }
}
