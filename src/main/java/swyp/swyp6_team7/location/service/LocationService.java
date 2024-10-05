package swyp.swyp6_team7.location.service;

import org.springframework.stereotype.Service;
import swyp.swyp6_team7.location.dao.LocationDao;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.parser.CityParser;
import swyp.swyp6_team7.location.reader.CsvReader;

import java.io.IOException;
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

    public void importCities(String filename, LocationType locationType) throws IOException {
        List<Location> cities = csvReader.readByLine(filename, cityParser, locationType);
        cities.stream()
                .filter(city -> city != null && city.getLocationName() != null) // null 체크 추가
                .forEach(city -> locationDao.addCity(city));
    }

}
