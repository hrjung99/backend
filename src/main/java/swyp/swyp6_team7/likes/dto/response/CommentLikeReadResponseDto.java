package swyp.swyp6_team7.likes.dto.response;

import lombok.*;
import swyp.swyp6_team7.comment.domain.Comment;
import swyp.swyp6_team7.likes.domain.CommentLike;

@Getter
@AllArgsConstructor
public class CommentLikeReadResponseDto {
    private int commentNumber; // 좋아요가 눌린 댓글 번호
    private boolean liked;      // 현재 유저가 좋아요를 눌렀는지 여부
    private long likes;     // 댓글의 총 좋아요 수

    @Override
    public String toString() {
        return "CommentLikeReadResponseDto{" +
                "commentNumber=" + commentNumber +
                ", liked=" + liked +
                ", totalLikes=" + likes +
                '}';
    }
}