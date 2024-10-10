package swyp.swyp6_team7.likes.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.likes.domain.Like;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeCreateRequestDto {

    private String relatedType;  // 댓글 또는 게시물의 유형 ("comment" = 댓글, "community" = 게시물)
    private int relatedNumber; // 댓글 또는 게시물의 식별자
    private int userNumber;    // 작성자 식별자

    public LikeCreateRequestDto(String relatedType, int relatedNumber, int userNumber) {
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.userNumber = userNumber;
    }

    public Like toCommentLikeEntity() {
        return Like.builder()
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .userNumber(userNumber)
                .build();
    }

}
