package swyp.swyp6_team7.location.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private int cityid;
    @Column(name = "country_name")
    private String countryName;
    @Column(name = "city_name")
    private String cityName;
    @Enumerated(EnumType.STRING)
    @Column(name = "city_type")
    private CityType cityType;

    @Builder
    public City(String countryName,String cityName, CityType cityType) {
        this.countryName = countryName;
        this.cityName = cityName;
        this.cityType = cityType;
    }
}
