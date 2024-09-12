package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.domain.TravelStatus;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.dto.request.TravelUpdateRequest;
import swyp.swyp6_team7.travel.dto.response.TravelSimpleDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

@RequiredArgsConstructor
@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public Travel save(TravelCreateRequest request, int userNumber) {
        return travelRepository.save(request.toEntity(userNumber));
    }

    public Travel getByNumber(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 임시저장, 삭제 상태에 따른 처리 추가

        return travel;
    }

    @Transactional
    public Travel update(int travelNumber, TravelUpdateRequest travelUpdate) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 작성자와 요청자 대조(인가)

        travel = travel.update(travelUpdate);
        return travelRepository.save(travel);
    }

    @Transactional
    public void delete(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 작성자와 요청자 대조(인가)

        travel.delete();
    }


    public Page<TravelSimpleDto> getPagedTravels(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Travel> travelPage = travelRepository.findByStatusIsNotOrderByCreatedAtDesc(TravelStatus.DRAFT, pageable);
        return travelPage.map(TravelSimpleDto::from);
    }

}
