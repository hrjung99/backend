package swyp.swyp6_team7.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import swyp.swyp6_team7.tag.domain.TravelTag;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.List;

public interface TravelTagRepository extends JpaRepository<TravelTag, Long> {

    @Query("select t from TravelTag t join fetch t.tag where t.travel.number = :travelNumber")
    List<TravelTag> findTagsByTravelNumber(@Param("travelNumber") int travelNumber);

    void deleteByTravel(Travel travel);
}
