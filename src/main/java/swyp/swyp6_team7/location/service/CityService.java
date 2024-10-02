package swyp.swyp6_team7.location.service;

import org.springframework.stereotype.Service;
import swyp.swyp6_team7.location.dao.CityDao;
import swyp.swyp6_team7.location.domain.City;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.parser.CityParser;
import swyp.swyp6_team7.location.reader.CsvReader;

import java.io.IOException;
import java.util.List;

@Service
public class CityService {
    private final CsvReader<City> csvReader;
    private final CityDao cityDao;
    private final CityParser cityParser;

    public CityService(CsvReader csvReader, CityDao cityDao, CityParser cityParser) {
        this.csvReader = csvReader;
        this.cityDao = cityDao;
        this.cityParser = cityParser;
    }

    public void importCities(String filename, CityType cityType) throws IOException {
        List<City> cities = csvReader.readByLine(filename, cityParser, cityType);
        cities.forEach(city -> cityDao.addCity(city));
    }
}
