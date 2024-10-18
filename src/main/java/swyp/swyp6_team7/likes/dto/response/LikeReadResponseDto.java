package swyp.swyp6_team7.likes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeReadResponseDto {
    private String relatedType;
    private int relatedNumber; // 좋아요가 눌린 댓글 또는 게시물 번호
    private boolean liked;      // 현재 유저가 좋아요를 눌렀는지 여부
    private long totalLikes;    // 댓글 또는 게시물의 총 좋아요 수

    @Override
    public String toString() {
        return "CommentLikeReadResponseDto{" +
                "relatedType='" + relatedType + '\'' +
                "relatedNumber=" + relatedNumber +
                ", liked=" + liked +
                ", totalLikes=" + totalLikes +
                '}';
    }
}
