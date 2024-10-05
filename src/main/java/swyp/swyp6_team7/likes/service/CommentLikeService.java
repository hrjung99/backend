package swyp.swyp6_team7.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.comment.repository.CommentRepository;
import swyp.swyp6_team7.comment.service.CommentService;
import swyp.swyp6_team7.likes.domain.CommentLike;
import swyp.swyp6_team7.likes.dto.response.CommentLikeReadResponseDto;
import swyp.swyp6_team7.likes.repository.CommentLikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    //Create
    //좋아요
    @Transactional
    public List<CommentListReponseDto> toggleLike(int commentNumber, int userNumber) {

        // 댓글 존재 여부 검증 검증
        Comment comment = commentRepository.findByCommentNumber(commentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다: " + commentNumber));

        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        if (liked) { // 이미 좋아요를 누른 경우
            // 좋아요 취소
            Optional<CommentLike> commentLike = commentLikeRepository.findByCommentNumberAndUserNumber(commentNumber, userNumber);
            commentLike.ifPresent(cl -> {
                commentLikeRepository.delete(cl); // 좋아요 삭제
            });
        } else { // 좋아요를 누르지 않은 경우
            // 좋아요 추가
            CommentLike commentLike = new CommentLike(commentNumber, userNumber);
            commentLikeRepository.save(commentLike);
        }
        return commentService.getListByrelatedNumber(comment.getRelatedType(), comment.getRelatedNumber(), userNumber);
    }

}
