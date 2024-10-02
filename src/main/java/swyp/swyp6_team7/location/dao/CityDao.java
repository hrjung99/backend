package swyp.swyp6_team7.location.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import swyp.swyp6_team7.location.domain.City;

@Repository
public class CityDao {
    private final JdbcTemplate jdbcTemplate;

    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addCity(City city) {
        String sql = "INSERT INTO cities (country_name, city_name, city_type) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, city.getCountryName(), city.getCityName(), city.getCityType().name());
    }
}
