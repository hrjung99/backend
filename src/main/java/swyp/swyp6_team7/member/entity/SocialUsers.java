package swyp.swyp6_team7.member.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Social_Users")
public class SocialUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer socialNumber;  // 자동 증가, 정수 타입으로 수정

    private String socialLoginId;  // 소셜 로그인 제공자의 고유 ID

    @Column(nullable = false, length = 320)
    private String socialEmail;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, length = 10)
    private String socialProvider;
}
