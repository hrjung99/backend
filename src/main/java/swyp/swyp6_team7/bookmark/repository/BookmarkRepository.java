package swyp.swyp6_team7.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.bookmark.entity.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {
    List<Bookmark> findByUserNumber(Integer userNumber);

    int countByUserNumber(Integer userNumber);
}
