package swyp.swyp6_team7.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int userNumber;

    @Column(nullable = false)
    private String deletedUserEmail;

    @Column(nullable = false)
    private LocalDateTime deletedUserRegDate;

    private LocalDateTime deletedUserLoginDate;

    @Column(nullable = false)
    private LocalDateTime deletedUserDeleteDate;



}
