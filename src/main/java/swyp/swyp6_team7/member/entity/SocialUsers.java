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
    private int socialNumber;

    @Column(nullable = false, length = 320)
    private String socialEmail;

    @ManyToOne
    @JoinColumn(name = "user_number", nullable = false)
    private Users user;

    @Column(nullable = false, length = 10)
    private String socialProvider;
}
