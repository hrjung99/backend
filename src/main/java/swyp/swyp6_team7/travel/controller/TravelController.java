package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.TravelSearchCondition;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.dto.request.TravelUpdateRequest;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.dto.response.TravelSimpleDto;
import swyp.swyp6_team7.travel.service.TravelService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TravelController {

    private final TravelService travelService;

    @PostMapping("/api/travel")
    public ResponseEntity<TravelDetailResponse> create(
            @RequestBody @Validated TravelCreateRequest request
    ) {
        //TODO: 토큰으로 유저 number 가져오는 작업 추가
        int userNumber = 1;
        String userName = "testName";

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(travelService.create(request, userNumber));
    }

    @GetMapping("/api/travel/detail/{travelNumber}")
    public ResponseEntity<TravelDetailResponse> getDetailsByNumber(@PathVariable("travelNumber") int travelNumber) {
        //TODO: 작성자 정보 가져오기 by userNumber
        int userNumber = 1;
        String userName = "testName";

        return ResponseEntity.status(HttpStatus.OK)
                .body(travelService.getDetailsByNumber(travelNumber));
    }

    @PutMapping("/api/travel/{travelNumber}")
    public ResponseEntity<TravelDetailResponse> update(
            @PathVariable("travelNumber") int travelNumber,
            @RequestBody TravelUpdateRequest request) {

        //TODO: 작성자 정보 가져오기 by userNumber
        int userNumber = 1;
        String userName = "testName";

        TravelDetailResponse updatedTravel = travelService.update(travelNumber, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedTravel);
    }

    @DeleteMapping("/api/travel/{travelNumber}")
    public ResponseEntity delete(@PathVariable("travelNumber") int travelNumber) {
        travelService.delete(travelNumber);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/api/travels/search")
    public ResponseEntity search(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "keyword") String keyword
    ) {

        TravelSearchCondition condition = TravelSearchCondition.builder()
                .keyword(keyword)
                .pageRequest(PageRequest.of(page, size))
                .build();

        Page<TravelSimpleDto> travels = travelService.search(condition);
        return ResponseEntity.status(HttpStatus.OK)
                .body(travels);
    }

}
