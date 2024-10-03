package swyp.swyp6_team7.location.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.location.domain.Location;

@Repository
public class LocationDao {
    private final JdbcTemplate jdbcTemplate;

    public LocationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCity(Location location) {
        String sql = "INSERT INTO locations (location_name, location_type) VALUES (?, ?)";
        jdbcTemplate.update(sql, location.getLocationName(), location.getLocationType().name());
    }
}
