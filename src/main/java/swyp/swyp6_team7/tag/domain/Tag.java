package swyp.swyp6_team7.tag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tags")
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_number", updatable = false)
    private Long number;

    @Column(name = "tag_name", nullable = false, unique = true, length = 20)
    private String name;

    @Builder
    public Tag(Long number, String name) {
        this.number = number;
        this.name = name;
    }

    public static Tag of(String name) {
        return Tag.builder()
                .name(name)
                .build();
    }

}
