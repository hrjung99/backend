package swyp.swyp6_team7.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.location.domain.City;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByCityName(String cityName);
}
