package swyp.swyp6_team7.member.entity;
import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import swyp.swyp6_team7.member.dto.UserRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @Column(nullable = false, length = 255)
    private String userPw;

    @Column(nullable = false, length = 50)
    private String userName;

    // 성별 ENUM으로 관리
    public enum Gender{
        M,F
    }

    @Column(nullable = false, length = 2)
    private Gender userGender;

    @Column(nullable = false, length = 5)
    private String userBirthYear;

    @Column(nullable = false, length = 15)
    private String userPhone;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime userRegDate = LocalDateTime.now();

    private LocalDateTime userLoginDate;
    private LocalDateTime userLogoutDate;

    @Builder.Default
    @Column(nullable = false, length = 10)
    private String userRole = "user";

    // 회원 상태 enum으로 관리
    public enum MemberStatus{
        ABLE, DELETED, SLEEP
    }
    @Column(nullable = false, length = 10)
    private MemberStatus userStatus;

    @Builder.Default
    @Column(nullable = false)
    private Boolean userSocialTF = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "userNumber"))
    @Column(name = "role")
    private List<String> roles;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role) // Lambda expression for GrantedAuthority
                .collect(Collectors.toList());
    }

    // Setters and Getters
    public void setPassword(String password) {
        this.userPw = password;
    }
    public String getEmail() {
        return this.userEmail;
    }
    public Integer getId() {
        return this.userNumber;
    }

    public boolean isAccountNonExpired() {
        return true;
    }


    public boolean isAccountNonLocked() {
        return true;
    }


    public boolean isCredentialsNonExpired() {
        return true;
    }


    public boolean isEnabled() {
        return true;
    }



}
