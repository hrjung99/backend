package swyp.swyp6_team7.likes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.likes.domain.CommentLike;
import swyp.swyp6_team7.likes.dto.response.CommentLikeReadResponseDto;
import swyp.swyp6_team7.likes.repository.CommentLikeRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    //댓글 조회 시 필요한 좋아요 상태
    public CommentLikeReadResponseDto getCommentLikeStatus(int commentNumber, int userNumber) {
        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        // 총 좋아요 수 가져오기
        long likes = commentLikeRepository.countByCommentNumber(commentNumber);

        // DTO 생성 및 반환
        return new CommentLikeReadResponseDto(commentNumber, liked, likes);
    }
}
