package swyp.swyp6_team7.likes.util;

import swyp.swyp6_team7.likes.dto.response.CommentLikeReadResponseDto;
import swyp.swyp6_team7.likes.repository.CommentLikeRepository;

public class CommentLikeStatus {



    //댓글 조회 시 필요한 좋아요 상태
    public static CommentLikeReadResponseDto getCommentLikeStatus(CommentLikeRepository commentLikeRepository, int commentNumber, int userNumber) {
        // 좋아요 여부 확인
        boolean liked = commentLikeRepository.existsByCommentNumberAndUserNumber(commentNumber, userNumber);

        // 총 좋아요 수 가져오기
        long likes = commentLikeRepository.countByCommentNumber(commentNumber);

        // DTO 생성 및 반환
        return new CommentLikeReadResponseDto(commentNumber, liked, likes);
    }
}
