package swyp.swyp6_team7.travel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;

public interface TravelCustomRepository {

    Page<TravelRecentDto> findAllSortedByCreatedAt(PageRequest pageRequest);

    Page<Travel> search(TravelSearchCondition condition);

}
