package swyp.swyp6_team7.location.parser;

import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.dao.CityDao;
import swyp.swyp6_team7.location.parser.Parser;
import swyp.swyp6_team7.location.domain.City;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.reader.CsvReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
public class CityParser implements Parser<City> {

    private final CityDao cityDao;

    public CityParser(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public City parse(String line, CityType cityType) {
        String[] columns = line.split(",");

        String country;
        String city;

        switch (cityType) {
            case DOMESTIC:
                country = columns[1].trim(); // 나라
                city = columns[3].trim(); // 소분류 (시)
                // '시' 제거 로직 추가
                if (city.endsWith("시")) {
                    city = city.substring(0, city.length() - 1);
                }
                break;

            case INTERNATIONAL:
                country = columns[2].trim(); // 중분류 (나라)
                city = columns[3].trim(); // 소분류 (도시)
                break;

            default:
                throw new IllegalArgumentException("지원되지 않는 CityType입니다: " + cityType);
        }

        return new City(country, city, cityType);
    }

    public void parseAndSave(String filePath, CityType cityType) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                City cityObject = parse(line, cityType);
                cityDao.addCity(cityObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
