package swyp.swyp6_team7.comment.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import swyp.swyp6_team7.travel.domain.Travel;

import java.time.LocalDateTime;

@Getter
@Table(name = "Comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_number", updatable = false, nullable = false)
    private int commentNumber;


    //작성자 식별자
    @Column(name = "user_number", nullable = false)
    private int userNumber;

    //댓글 내용
    @Column(name = "comment_content", length = 1000, nullable = false)
    private String content;

    //부모 댓글 번호
    @Column(name = "comment_parent_number", nullable = false, updatable = false)
    private int parentNumber;

    //좋아요
    @Column(name = "comment_likes", nullable = false)
    private int likes;

    //작성 일시
    @CreatedDate
    @Column(name = "comment_reg_date", nullable = false)
    private LocalDateTime regDate;

    //게시물 타입 식별자
    @Column(name = "comment_related_type", nullable = false, updatable = false)
    private String relatedType;


    //게시물 번호 식별자
    @Column(name = "comment_related_number", nullable = false, updatable = false)
    private int relatedNumber;

    //C
    @Builder
    public Comment (int userNumber, String content, int parentNumber, int likes, LocalDateTime regDate, String relatedType, int relatedNumber) {
        this.userNumber = userNumber;
        this.content = content;
        this.parentNumber = parentNumber;
        this.likes = likes;
        this.regDate = regDate;
        this.relatedType = relatedType;
        this.relatedNumber = relatedNumber;
    }

    //U
    public Comment update(String content) {
        this.content = content;
        return this;
    }

    //D
    public Comment delete(int commentNumber) {
        this.commentNumber = commentNumber;
        return this;
    }

//    // 외래 키 관계 설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_related_number", referencedColumnName = "travel_number", insertable = false, updatable = false)
//    private Travel number; // 여행 관련 외래 키

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "related_number", referencedColumnName = "post_number", insertable = false, updatable = false)
//    private CommunityPost communityPost; // 커뮤니티 관련 외래 키

}
