package swyp.swyp6_team7.comment.dto.request;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateRequestDto {

    @Size(max = 1000)
    private String content;
    private int parentNumber;




    public CommentCreateRequestDto(
            String content, int parentNumber) {
        this.content = content;
        this.parentNumber = parentNumber;

    }

    public Comment toCommentEntity(int userNumber, String content, int parentNumber, LocalDateTime regDate, String relatedType, int relatedNumber) {
        return Comment.builder()
                .userNumber(userNumber)
                .content(content)
                .parentNumber(parentNumber)
                .regDate(LocalDateTime.now())
                .relatedType(relatedType)
                .relatedNumber(relatedNumber)
                .build();

    }
}
