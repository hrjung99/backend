package swyp.swyp6_team7.location.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class City {
    private String countryName;
    private String cityName;
    private CityType cityType;
}
