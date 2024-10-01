package swyp.swyp6_team7.bookmark.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.bookmark.dto.BookmarkRequest;
import swyp.swyp6_team7.bookmark.dto.BookmarkResponse;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;
import swyp.swyp6_team7.bookmark.repository.BookmarkRepository;
import swyp.swyp6_team7.companion.repository.CompanionRepository;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static swyp.swyp6_team7.bookmark.dto.BookmarkResponse.formatDDay;
import static swyp.swyp6_team7.bookmark.dto.BookmarkResponse.formatPostedAgo;

@AllArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    @Transactional
    public void addBookmark(BookmarkRequest request) {

        Users user = userRepository.findById(request.getUserNumber())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Travel travel = travelRepository.findById(request.getTravelNumber())
                .orElseThrow(() -> new IllegalArgumentException("여행 정보를 찾을 수 없습니다."));

        // 북마크 개수 제한 확인
        int bookmarkCount = bookmarkRepository.countByUserNumber(request.getUserNumber());
        if (bookmarkCount >= 30) {
            // 가장 오래된 북마크 삭제
            List<Bookmark> oldestBookmarks = bookmarkRepository.findOldestByUserNumber(request.getUserNumber());
            bookmarkRepository.delete(oldestBookmarks.get(0));
        }

        // 북마크 저장
        Bookmark bookmark = new Bookmark(
                user.getUserNumber(),
                travel.getNumber(),
                LocalDateTime.now() // bookmarkDate 설정
        );
        bookmarkRepository.save(bookmark);
    }

    @Transactional
    public void removeBookmark(Integer travelNumber, Integer userNumber) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByUserNumber(userNumber);

        // 특정 travelNumber와 일치하는 북마크 찾기
        Bookmark bookmark = bookmarks.stream()
                .filter(b -> b.getTravelNumber().equals(travelNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("북마크를 찾을 수 없습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<BookmarkResponse> getBookmarksByUser(Integer userNumber) {
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksByUserNumber(userNumber);

        // 저장된 콘텐츠가 없을 때 빈 리스트 처리
        if (bookmarks.isEmpty()) {
            return List.of();
        }

        // 북마크 목록을 조회하면서 각 여행 정보 가져오기
        return bookmarks.stream().map(bookmark -> {
            Travel travel = travelRepository.findById(bookmark.getTravelNumber())
                    .orElseThrow(() -> new IllegalArgumentException("여행 정보를 찾을 수 없습니다."));

            String dDay = formatDDay(travel.getDueDate());
            String postedAgo = formatPostedAgo(travel.getCreatedAt().toLocalDate());

            Users user = userRepository.findById(userNumber)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            int currentApplicants = travel.getCompanions().size();

            List<String> tags = travel.getTravelTags().stream()
                    .map(travelTag -> travelTag.getTag().getName())
                    .collect(Collectors.toList());

            return new BookmarkResponse(
                    travel.getNumber(),
                    true,
                    travel.getTitle(),
                    travel.getLocation(),
                    user.getUserName(),
                    postedAgo,
                    dDay,
                    currentApplicants,
                    travel.getMaxPerson(),
                    travel.getStatus() == TravelStatus.CLOSED,
                    tags,
                    "/api/travel/" + travel.getNumber(),
                    "/api/bookmarks/" + travel.getNumber()
            );
        }).collect(Collectors.toList());
    }
    // 콘텐츠의 상세 URL을 생성하는 메서드
    private String generateDetailUrl(Bookmark bookmark) {
        return "/api/travel/" + bookmark.getTravelNumber();
    }

    // 북마크 제거 URL을 생성하는 메서드
    private String generateRemoveBookmarkUrl(Bookmark bookmark) {
        return "/api/bookmarks/" + bookmark.getTravelNumber();
    }
    // D-Day 형식으로 마감기한 포맷팅하는 메서드
    private String formatDDay(LocalDate dueDate) {
        long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        return "마감 D-" + daysUntil;
    }

    // 작성일로부터 경과한 시간을 포맷팅하는 메서드
    private String formatPostedAgo(LocalDate createdAt) {
        long daysAgo = ChronoUnit.DAYS.between(createdAt, LocalDate.now());
        if (daysAgo == 0) {
            return "오늘";
        } else {
            return daysAgo + "일 전";
        }
    }
}
