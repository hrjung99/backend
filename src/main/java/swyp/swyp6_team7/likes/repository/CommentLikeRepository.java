package swyp.swyp6_team7.likes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.likes.domain.CommentLike;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    // 좋아요 누름 여부 확인
    boolean existsByCommentNumberAndUserNumber(int commentNumber, int userNumber);

    // 특정 댓글의 좋아요 수
    long countByCommentNumber(int commentNumber);

    //댓글 번호로 좋아요 기록 삭제
    void deleteByCommentNumber(int commentNumber);

    //좋아요 행 조회
    Optional<CommentLike> findByCommentNumberAndUserNumber(int commentNumber, int userNumber);
}
