package swyp.swyp6_team7.companion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.companion.domain.Companion;
import swyp.swyp6_team7.enrollment.domain.EnrollmentStatus;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.Optional;
import java.util.List;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    Optional<Companion> findByTravelAndUserNumber(Travel travel, int userNumber);

    List<Companion> findByUserNumber(Integer userNumber);

    void deleteByTravelAndUserNumber(Travel travel, int userNumber);
}
