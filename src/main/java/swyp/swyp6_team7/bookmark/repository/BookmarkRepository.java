package swyp.swyp6_team7.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.swyp6_team7.bookmark.entity.Bookmark;
import swyp.swyp6_team7.bookmark.entity.ContentType;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    List<Bookmark> findByUserNumber(Integer userNumber);

    int countByUserNumber(Integer userNumber);

    int countByContentIdAndContentType(int travelNumber, ContentType type);

    // 가장 오래된 북마크 조회
    @Query("SELECT b FROM Bookmark b WHERE b.userNumber = :userNumber ORDER BY b.bookmarkDate ASC")
    List<Bookmark> findOldestByUserNumber(@Param("userNumber") Integer userNumber);

    // 특정 사용자가 특정 콘텐츠를 북마크했는지 확인
    boolean existsByUserNumberAndContentIdAndContentType(Integer userNumber, Integer contentId, ContentType contentType);
    boolean deleteByUserNumberAndContentIdAndContentType(Integer userNumber, Integer contentId, ContentType contentType);

}
