package swyp.swyp6_team7.travel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.tag.service.TravelTagService;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.dto.request.TravelUpdateRequest;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelSearchDto;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final TravelTagService travelTagService;

    @Transactional
    public TravelDetailResponse create(TravelCreateRequest request, int userNumber) {
        Travel savedTravel = travelRepository.save(request.toTravelEntity(userNumber));
        List<String> tags = travelTagService.create(savedTravel, request.getTags()).stream()
                .map(tag -> tag.getName())
                .toList();

        return TravelDetailResponse.from(savedTravel, tags, userNumber, "username");
    }

    public TravelDetailResponse getDetailsByNumber(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 임시저장, 삭제 상태에 따른 처리 추가

        List<String> tags = travelTagService.getTagsByTravelNumber(travel.getNumber());

        return TravelDetailResponse.from(travel, tags, travel.getUserNumber(), "username");
    }

    @Transactional
    public TravelDetailResponse update(int travelNumber, TravelUpdateRequest travelUpdate) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 작성자와 요청자 대조(인가)

        Travel updatedTravel = travel.update(travelUpdate);
        List<String> updatedTags = travelTagService.update(updatedTravel, travelUpdate.getTags());

        return TravelDetailResponse.from(updatedTravel, updatedTags, updatedTravel.getUserNumber(), "username");
    }

    @Transactional
    public void delete(int travelNumber) {
        Travel travel = travelRepository.findByNumber(travelNumber)
                .orElseThrow(() -> new IllegalArgumentException("travel not found: " + travelNumber));

        //TODO: 작성자와 요청자 대조(인가)

        travel.delete();
    }


    public Page<TravelSearchDto> search(TravelSearchCondition condition) {
        Page<TravelSearchDto> result = travelRepository.search(condition);
        for (TravelSearchDto travelSearchDto : result) {
            log.info("service: " + travelSearchDto.toString());
        }
        return result;
    }
}
