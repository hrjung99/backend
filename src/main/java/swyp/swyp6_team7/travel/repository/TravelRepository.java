package swyp.swyp6_team7.travel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Integer>, TravelCustomRepository {
    Optional<Travel> findByNumber(Integer integer);

}