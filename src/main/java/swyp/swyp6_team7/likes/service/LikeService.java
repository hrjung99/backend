package swyp.swyp6_team7.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.comment.dto.response.CommentListReponseDto;
import swyp.swyp6_team7.comment.repository.CommentRepository;
import swyp.swyp6_team7.comment.service.CommentService;
import swyp.swyp6_team7.likes.domain.Like;
import swyp.swyp6_team7.likes.repository.LikeRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikeService {

    private final LikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public List<CommentListReponseDto> toggleLike(String relatedType, int relatedNumber, int userNumber) {

        //댓글 좋아요의 경우
        if ("comment".equals(relatedType)) {
            // 댓글 존재 여부 검증
            Comment comment = commentRepository.findByCommentNumber(relatedNumber)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다: " + relatedNumber));

            // 좋아요 여부 확인
            boolean liked = commentLikeRepository.existsByRelatedTypeAndRelatedNumberAndUserNumber(relatedType, relatedNumber, userNumber);

            if (liked) { // 이미 좋아요를 누른 경우
                // 좋아요 취소
                Optional<Like> commentLike = commentLikeRepository.findByRelatedTypeAndRelatedNumberAndUserNumber(relatedType, relatedNumber, userNumber);
                commentLike.ifPresent(cl -> {
                    commentLikeRepository.delete(cl); // 좋아요 삭제
                });
            } else { // 좋아요를 누르지 않은 경우
                // 좋아요 추가
                Like commentLike = new Like(relatedType, relatedNumber, userNumber);
                commentLikeRepository.save(commentLike);
            }
            return commentService.getList(comment.getRelatedType(), comment.getRelatedNumber(), userNumber);

            //게시물 좋아요의 경우
        } else if ("community".equals(relatedType)) {
            // TODO: 커뮤니티 좋아요 로직 추가
            log.info("커뮤니티 좋아요 로직이 아직 구현되지 않았습니다.");
            // 나중에 구현할 로직을 여기에 추가하세요
            return List.of(); // 적절한 값을 반환하도록 수정
        } else {
            throw new IllegalArgumentException("유효하지 않은 relatedType: " + relatedType);
        }
    }
}
