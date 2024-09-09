package swyp.swyp6_team7.member.entity;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNumber;

    @Column(nullable = false, unique = true, length = 255)
    private String userEmail;

    @Column(nullable = false, length = 50)
    private String userFirstName;

    @Column(nullable = false, length = 50)
    private String userLastName;

    @Column(nullable = false, length = 2)
    private String userGender;

    @Column(nullable = false)
    private String userBirthYear;

    @Column(nullable = false, length = 255)
    private String userPw;

    @Column(nullable = false, length = 15)
    private String userPhone;

    @Column(nullable = false)
    private LocalDateTime userRegDate = LocalDateTime.now();

    private LocalDateTime userLoginDate;
    private LocalDateTime userLogoutDate;

    @Column(nullable = false, length = 10)
    private String userRole = "user";

    @Column(nullable = false, length = 10)
    private String userStatus;

    @Column(nullable = false)
    private Boolean userSocialTF = false;
}
