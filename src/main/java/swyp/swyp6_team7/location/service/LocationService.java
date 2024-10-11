package swyp.swyp6_team7.location.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.location.dao.LocationDao;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.parser.CityParser;
import swyp.swyp6_team7.location.reader.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class LocationService {
    private final CsvReader<Location> csvReader;
    private final LocationDao locationDao;
    private final CityParser cityParser;

    public LocationService(CsvReader csvReader, LocationDao locationDao, CityParser cityParser) {
        this.csvReader = csvReader;
        this.locationDao = locationDao;
        this.cityParser = cityParser;
    }

    public void importCities(InputStream inputStream, LocationType locationType) throws IOException {
        List<Location> cities = csvReader.readByLine(inputStream, cityParser, locationType);
        cities.forEach(city -> {
            if (city != null && city.getLocationName() != null && !city.getLocationName().trim().isEmpty()) {
                locationDao.addCity(city);
            } else {
                System.out.println("Invalid location data: " + city);
            }
        });
    }

    public void loadAllLocations() throws IOException {
        // ClassPathResource를 사용하여 InputStream을 가져옵니다.
        try (InputStream domesticStream = new ClassPathResource("cities/korea_cities.csv").getInputStream();
             InputStream internationalStream = new ClassPathResource("cities/foreign_cities.csv").getInputStream()) {

            importCities(domesticStream, LocationType.DOMESTIC);
            importCities(internationalStream, LocationType.INTERNATIONAL);
        }
    }

}
