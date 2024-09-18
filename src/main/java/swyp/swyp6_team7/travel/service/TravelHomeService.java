package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelHomeService {

    private final TravelRepository travelRepository;

    public List<TravelRecentDto> getTravelsSortedByCreatedAt() {
        return travelRepository.findAllSortedByCreatedAt();
    }

}
