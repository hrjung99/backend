package swyp.swyp6_team7.travel.repository;

import org.springframework.data.domain.Page;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;

public interface TravelCustomRepository {

    Page<Travel> search(TravelSearchCondition condition);

}
