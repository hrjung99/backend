package swyp.swyp6_team7.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Integer> {
    Optional<Travel> findByNumber(Integer integer);
}
