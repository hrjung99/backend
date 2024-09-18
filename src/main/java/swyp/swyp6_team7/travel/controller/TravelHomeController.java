package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.travel.dto.response.TravelRecentDto;
import swyp.swyp6_team7.travel.service.TravelHomeService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TravelHomeController {

    private final TravelHomeService travelHomeService;


    @GetMapping("/api/travels/recent")
    public ResponseEntity getRecentlyCreatedTravels(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        List<TravelRecentDto> result = travelHomeService.getTravelsSortedByCreatedAt();

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

}
