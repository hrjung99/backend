package swyp.swyp6_team7.location.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;

@Repository
public class LocationDao {
    private final JdbcTemplate jdbcTemplate;

    public LocationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCity(Location location) {
        if (location == null || location.getLocationName() == null) {
            throw new IllegalArgumentException("Invalid location data: location or locationName is null");
        }

        // 중복 데이터 체크
        if (isLocationExists(location.getLocationName(), location.getLocationType())) {
            System.out.println("Duplicate entry found for: " + location.getLocationName());
            return; // 이미 존재하는 경우 삽입하지 않음
        }

        String sql = "INSERT INTO locations (location_name, location_type) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, location.getLocationName(), location.getLocationType().name());
        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate entry found for: " + location.getLocationName());
        }
    }

    public boolean isLocationExists(String locationName, LocationType locationType) {
        // locationName과 locationType을 기준으로 DB에 해당 레코드가 있는지 체크합니다.
        String sql = "SELECT COUNT(*) FROM locations WHERE location_name = ? AND location_type = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{locationName, locationType.name()}, Integer.class);
        return count > 0;
    }
}
