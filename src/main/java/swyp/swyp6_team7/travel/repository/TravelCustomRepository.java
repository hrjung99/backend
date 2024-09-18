package swyp.swyp6_team7.travel.repository;

import org.springframework.data.domain.Page;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;

import java.util.List;

public interface TravelCustomRepository {

    List<TravelRecentDto> findAllSortedByCreatedAt();

    Page<Travel> search(TravelSearchCondition condition);

}
