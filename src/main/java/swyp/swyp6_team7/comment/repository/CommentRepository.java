package swyp.swyp6_team7.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.comment.domain.Comment;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    //댓글 조회 (댓글, 답글 전부 조회)
    List<Comment> findByRelatedTypeAndRelatedNumber(String relatedType, int relatedNumber);

    // 답글 개수 조회
    long countByRelatedTypeAndRelatedNumberAndParentNumber(String relatedType, int relatedNumber, int parentNumber);
    
    //댓글(혹은 답글) 한개 조회
    Optional<Comment> findByCommentNumber(int commentNumber);

}