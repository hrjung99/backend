package swyp.swyp6_team7.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.service.LocationService;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private LocationService locationService;

    @Override
    public void run(String... args) throws Exception {
        locationService.importCities("src/main/resources/korea_cities.csv", LocationType.DOMESTIC);
        locationService.importCities("src/main/resources/foreign_cities.csv", LocationType.INTERNATIONAL);
    }
}
