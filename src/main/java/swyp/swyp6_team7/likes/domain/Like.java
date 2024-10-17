package swyp.swyp6_team7.likes.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Table(name = "Likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeNumber;

    //댓글의 좋아요인지 커뮤니티 게시글의 좋아요인지 식별
    //comment / community
    @Column(name = "related_type", updatable = false, nullable = false)
    private String relatedType;

    //댓글 번호 혹은 게시글 번호
    @Column(name = "related_number", updatable = false, nullable = false)
    private int relatedNumber;


    //좋아요 누른 유저 번호
    @Column(name = "user_number", nullable = false, updatable = false)
    private int userNumber;

    // 좋아요 (create)
    @Builder
    public Like(String relatedType, int relatedNumber, int userNumber) {
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
        this.userNumber = userNumber;
    }

    // 좋아요 취소 (delete)
    public Like delete(int likeNumber) {
        this.likeNumber = likeNumber;
        return this;
    }
}
