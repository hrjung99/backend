package swyp.swyp6_team7.travel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;
import swyp.swyp6_team7.travel.dto.response.TravelSearchDto;

public interface TravelCustomRepository {

    Page<TravelRecentDto> findAllSortedByCreatedAt(PageRequest pageRequest);

    Page<TravelSearchDto> search(TravelSearchCondition condition);

    TravelDetailResponse getDetailsByNumber(int travelNumber);

}
