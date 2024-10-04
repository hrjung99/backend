package swyp.swyp6_team7.likes.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.likes.domain.CommentLike;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLikeCreateRequestDto {

    private int commentNumber;
    private int userNumber;

    public CommentLikeCreateRequestDto(int commentNumber, int userNumber) {
        this.commentNumber = commentNumber;
        this.userNumber = userNumber;
    }

    public CommentLike toCommentLikeEntity(int commentNumber, int userNumber) {
        return CommentLike.builder()
                .commentNumber(commentNumber)
                .userNumber(userNumber)
                .build();
    }

}
