package swyp.swyp6_team7.member.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Social_Users")
public class SocialUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer socialNumber;  // 자동 증가, 정수 타입으로 수정

    @Column(nullable = false, unique = true, length = 320)
    private String socialLoginId;  // 소셜 로그인 제공자의 고유 ID

    @Column(nullable = false, unique = true, length = 320)
    private String socialEmail;

    @ManyToOne
    @JoinColumn(name = "user_number", nullable = false)
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SocialProvider socialProvider;

    @Builder
    public SocialUsers(String socialLoginId, String socialEmail, Users user, SocialProvider socialProvider) {
        this.socialLoginId = socialLoginId;
        this.socialEmail = socialEmail;
        this.user = user;
        this.socialProvider = socialProvider;
    }
}
