package swyp.swyp6_team7.companion.repository;

import swyp.swyp6_team7.companion.dto.CompanionInfoDto;

import java.util.List;

public interface CompanionCustomRepository {

    List<CompanionInfoDto> findCompanionInfoByTravelNumber(int travelNumber);

}
