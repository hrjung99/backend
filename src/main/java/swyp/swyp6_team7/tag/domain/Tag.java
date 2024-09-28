package swyp.swyp6_team7.tag.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tags")
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_number", updatable = false)
    private int number;

    @Column(name = "tag_name", nullable = false, unique = true, length = 20)
    private String name;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTagPreference> userTagPreferences;  // user_tagpreferences 참조

    @Builder
    public Tag(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public static Tag of(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }

}
