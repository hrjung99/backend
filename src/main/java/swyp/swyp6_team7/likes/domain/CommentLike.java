package swyp.swyp6_team7.likes.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Table(name = "Comment_Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeNumber;

    //댓글 식별자
    @Column(name = "comment_number", updatable = false, nullable = false)
    private int commentNumber;


    //작성자 식별자
    @Column(name = "user_number", nullable = false, updatable = false)
    private int userNumber;

    // 좋아요 (create)
    @Builder
    public CommentLike create(int commentNumber, int userNumber) {
        this.commentNumber = commentNumber;
        this.userNumber = userNumber;
        return this;
    }

    // 좋아요 취소 (delete)
    public CommentLike delete(int likeNumber) {
        this.likeNumber = likeNumber;
        return this;
    }
}
