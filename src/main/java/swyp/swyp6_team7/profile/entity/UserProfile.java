package swyp.swyp6_team7.profile.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import swyp.swyp6_team7.tag.domain.Tag;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proNumber;

    @Column(nullable = false)
    private Integer userNumber;

    @Column(length = 200)
    private String proIntroduce;

    private String profileImageUrl;  // 프로필 이미지 URL

    @ManyToMany
    @JoinTable(
            name = "user_tag_preferences",
            joinColumns = @JoinColumn(name = "user_number"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> preferredTags;  // 선호 태그




}
