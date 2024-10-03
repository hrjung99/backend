package swyp.swyp6_team7.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.location.domain.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByLocationName(String cityName);
}
