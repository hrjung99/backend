package swyp.swyp6_team7.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Deleted_Users")
public class DeletedUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deletedNumber;

    @Column(nullable = false)
    private Integer userNumber;

    @Column(nullable = false)
    private String deletedUserEmail;

    @Column(nullable = false)
    private LocalDate deletedUserDeleteDate; // 탈퇴한 날짜

    private LocalDateTime deletedUserLoginDate; // 마지막 로그인한 날짜

    @Column(nullable = false)
    private LocalDate finalDeletionDate; // 최종 삭제 처리할 날짜(탈퇴 3개월 뒤)

    // 모든 필드를 초기화하는 생성자
    public DeletedUsers(String deletedUserEmail, LocalDate deletedUserDeleteDate,
                        LocalDate finalDeletionDate, Integer userNumber) {
        this.deletedUserEmail = deletedUserEmail;
        this.deletedUserDeleteDate = deletedUserDeleteDate;
        this.finalDeletionDate = finalDeletionDate;
        this.userNumber = userNumber;
    }

}
