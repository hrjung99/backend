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
        int bookmarkCount = bookmarkRepository.countByUserNumber(request.getUserNumber());
        if (bookmarkCount >= 30) {
            // 가장 오래된 북마크 삭제
            List<Bookmark> oldestBookmarks = bookmarkRepository.findOldestByUserNumber(request.getUserNumber());
            bookmarkRepository.delete(oldestBookmarks.get(0));
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

        // 저장된 콘텐츠가 없을 때 빈 리스트 처리
        if (bookmarks.isEmpty()) {
            // 빈 리스트 처리 로직 추가 (메시지 표시 및 추천 콘텐츠 노출 등)
            return List.of();
        }

        // 여행 콘텐츠는 마감일 순으로 정렬
        List<BookmarkResponse> sortedBookmarks = bookmarks.stream()
                .sorted((b1, b2) -> {
                    if (b1.getContentType() == ContentType.TRAVEL && b2.getContentType() == ContentType.TRAVEL) {
                        // 여행 콘텐츠 정렬 기준 (마감일이 가까운 순서)
                        return b1.getBookmarkDate().compareTo(b2.getBookmarkDate());
                    }
                    // 여행이 아닌 경우는 기본 정렬 유지
                    return 0;
                })
                .map(bookmark -> new BookmarkResponse(
                        bookmark.getBookmarkId(),
                        bookmark.getContentId(),
                        bookmark.getContentType().name(),
                        bookmark.getBookmarkDate()
                ))
                .collect(Collectors.toList());

        return sortedBookmarks;
    }
}
