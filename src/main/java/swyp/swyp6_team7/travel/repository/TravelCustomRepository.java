package swyp.swyp6_team7.travel.repository;

import com.querydsl.core.Tuple;
import swyp.swyp6_team7.travel.domain.Travel;

import java.util.List;

public interface TravelCustomRepository {

    List<Travel> search(String title);

}
