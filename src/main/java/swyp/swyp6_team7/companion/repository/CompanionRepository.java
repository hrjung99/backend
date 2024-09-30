package swyp.swyp6_team7.companion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.Optional;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    Optional<Companion> findByTravelAndUserNumber(Travel travel, int userNumber);

    void deleteByTravelAndUserNumber(Travel travel, int userNumber);
}
