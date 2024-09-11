package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.travel.domain.Travel;
import swyp.swyp6_team7.travel.dto.request.TravelCreateRequest;
import swyp.swyp6_team7.travel.dto.response.TravelDetailResponse;
import swyp.swyp6_team7.travel.service.TravelService;

@RequiredArgsConstructor
@RestController
public class TravelController {

    private final TravelService travelService;

    @PostMapping("/api/travel")
    public ResponseEntity create(
            @RequestBody @Validated TravelCreateRequest request
    ) {
        //TODO: 토큰으로 유저 number 가져오는 작업 추가
        int userNumber = 1;
        String userName = "testName";

        Travel savedTravel = travelService.save(request, userNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TravelDetailResponse.from(savedTravel, userNumber, userName));
    }

}
