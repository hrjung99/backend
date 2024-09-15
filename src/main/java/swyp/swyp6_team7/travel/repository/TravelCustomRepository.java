package swyp.swyp6_team7.travel.repository;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.response.TravelSimpleDto;

import java.util.List;

public interface TravelCustomRepository {

    Page<Travel> search(TravelSearchCondition condition);

}
