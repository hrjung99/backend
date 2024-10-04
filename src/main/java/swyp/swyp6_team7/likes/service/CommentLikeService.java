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

    //Read
    //댓글 조회 시 필요한 좋아요 상태
    public CommentLikeReadResponseDto getCommentLikeStatus(int commentNumber, int userNumber) {
        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        // 총 좋아요 수 가져오기
        long likes = commentLikeRepository.countByCommentNumber(commentNumber);

        // DTO 생성 및 반환
        return new CommentLikeReadResponseDto(commentNumber, liked, likes);
    }

    //Create
    //좋아요
    @Transactional
    public List<CommentListReponseDto> like(int commentNumber, int userNumber) {

        // 댓글 존재 여부 검증 검증
        Comment comment = commentRepository.findByCommentNumber(commentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다: " + commentNumber));

        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        if (liked) { // 이미 좋아요를 누른 경우
            throw new IllegalArgumentException("유효하지않은 접근 입니다 : 이미 like 상태입니다.");
        }

        // 좋아요 추가
        CommentLike commentLike = new CommentLike(commentNumber, userNumber);
        commentLikeRepository.save(commentLike);

        //result
        List<CommentListReponseDto> result = new ArrayList<>();
        result = commentService.getListByrelatedNumber(comment.getRelatedType(), comment.getRelatedNumber(), userNumber);

        return result;
    }

    //좋아요 취소
    @Transactional
    public List<CommentListReponseDto> unlike(int commentNumber, int userNumber) {

        // 댓글 존재 여부 검증 검증
        Comment comment = commentRepository.findByCommentNumber(commentNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다: " + commentNumber));

        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        if (liked) { //좋아요를 누른 상태인 경우

            Optional<CommentLike> commentLike = commentLikeRepository.findByCommentNumberAndUserNumber(commentNumber, userNumber);
            commentLike.get().delete(commentLike.get().getLikeNumber());

            //result
            List<CommentListReponseDto> result = new ArrayList<>();
            result = commentService.getListByrelatedNumber(comment.getRelatedType(), comment.getRelatedNumber(), userNumber);

            return result;

        } else {
            throw new IllegalArgumentException("유효하지않은 접근 입니다 : 이미 unlike 상태입니다.");
        }

    }
}
