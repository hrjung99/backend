package swyp.swyp6_team7.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import swyp.swyp6_team7.member.entity.Users;

@Getter
@Setter
@Entity
@Table(name = "user_tagpreferences")
public class UserTagPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pre_tag_number")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number")
    private Users user;  // Users 엔티티 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_number")
    private Tag tag;  // Tag 엔티티 참조
}
