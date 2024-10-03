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
