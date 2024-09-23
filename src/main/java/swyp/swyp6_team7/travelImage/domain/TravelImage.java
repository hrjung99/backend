package swyp.swyp6_team7.travelImage.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "Travel_Images")
public class TravelImage {

    @Id //PK 필드 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PK 생성 규칙
    private int image_number;

    @Column(columnDefinition = "int", nullable = false)
    private int travel_number;

    @Column(columnDefinition = "varchar", length =255, nullable = false)
    private String travel_image_original_name;
    private String travel_image_real_name;

    @Column(columnDefinition = "varchar", length = 10, nullable = false)
    private String travel_image_format;

    @Column(columnDefinition = "bigint", nullable = false)
    private int travel_image_size;

    @Column(columnDefinition = "datetime", nullable = false)
    private String travel_image_reg_date;
}
