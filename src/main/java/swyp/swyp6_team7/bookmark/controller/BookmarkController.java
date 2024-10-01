package swyp.swyp6_team7.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.dto.BookmarkResponse;
import swyp.swyp6_team7.bookmark.service.BookmarkService;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final JwtProvider jwtProvider;

    public BookmarkController(BookmarkService bookmarkService,JwtProvider jwtProvider) {
        this.bookmarkService = bookmarkService;
        this.jwtProvider = jwtProvider;
    }

    // 북마크 추가
    @PostMapping
    public ResponseEntity<?> addBookmark(@RequestHeader("Authorization") String token,@RequestBody BookmarkRequest request) {
        // 토큰에서 userNumber 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // userNumber를 요청에 추가
        request.setUserNumber(userNumber);
        bookmarkService.addBookmark(request);
        return ResponseEntity.ok().build();
    }

    // 북마크 삭제
    @DeleteMapping("/{travelNumber}")
    public ResponseEntity<?> removeBookmark(@PathVariable("travelNumber") Integer travelNumber, @RequestHeader("Authorization") String token) {
        // 토큰에서 userNumber 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 여행 번호와 사용자 번호로 북마크 삭제
        bookmarkService.removeBookmark(travelNumber, userNumber);
        return ResponseEntity.noContent().build();
    }

    // 사용자의 북마크 목록 조회
    @GetMapping
    public ResponseEntity<List<BookmarkResponse>> getBookmarks(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");

        // JWT 토큰에서 사용자 ID 추출
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 사용자 ID로 북마크 조회
        List<BookmarkResponse> bookmarks = bookmarkService.getBookmarksByUser(userNumber);

        return ResponseEntity.ok(bookmarks);
    }
}