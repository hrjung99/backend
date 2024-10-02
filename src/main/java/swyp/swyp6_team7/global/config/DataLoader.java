package swyp.swyp6_team7.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.service.CityService;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private CityService cityService;

    @Override
    public void run(String... args) throws Exception {
        cityService.importCities("src/main/resources/korea_cities.csv", CityType.DOMESTIC);
        cityService.importCities("src/main/resources/foreign_cities.csv", CityType.INTERNATIONAL);
    }
}
