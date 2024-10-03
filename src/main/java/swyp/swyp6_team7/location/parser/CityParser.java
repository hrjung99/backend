package swyp.swyp6_team7.location.parser;

import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.dao.LocationDao;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class CityParser implements Parser<Location> {

    private final LocationDao locationDao;

    public CityParser(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @Override
    public Location parse(String line, LocationType locationType) {
        String[] columns = line.split(",");

        String location;

        switch (locationType) {
            case DOMESTIC:
                location = columns[3].trim(); // 소분류 (시)
                // '시' 제거 로직 추가
                if (location.endsWith("시")) {
                    location = location.substring(0, location.length() - 1);
                }
                break;

            case INTERNATIONAL:
                location = columns[2].trim(); // 중분류 (나라)
                break;

            default:
                throw new IllegalArgumentException("지원되지 않는 CityType입니다: " + locationType);
        }

        return new Location(location, locationType);
    }

    public void parseAndSave(String filePath, LocationType locationType) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Location locationObject = parse(line, locationType);
                locationDao.addCity(locationObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
