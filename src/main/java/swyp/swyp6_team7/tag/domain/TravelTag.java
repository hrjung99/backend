package swyp.swyp6_team7.tag.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import swyp.swyp6_team7.travel.domain.Travel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "travel_tags")
@Entity
public class TravelTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_tags_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_number", nullable = false)
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_number", nullable = false)
    private Tag tag;

    @Builder
    public TravelTag(Long number, Travel travel, Tag tag) {
        this.number = number;
        this.travel = travel;
        this.tag = tag;
    }

    public static TravelTag of(Travel travel, Tag tag) {
        return TravelTag.builder()
                .travel(travel)
                .tag(tag)
                .build();
    }
}
