package swyp.swyp6_team7.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.service.TravelListService;

import java.util.List;

@RestController
@RequestMapping("/api/my-travels")
public class TravelListController {
    private final TravelListService travelListService;
    private final JwtProvider jwtProvider;

    public TravelListController(TravelListService travelListService, JwtProvider jwtProvider) {
        this.travelListService = travelListService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("")
    public ResponseEntity<List<TravelListResponseDto>> getMyCreatedTravels(@RequestHeader("Authorization") String token) {
        // JWT 토큰에서 사용자 ID 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 여행 목록 조회
        List<TravelListResponseDto> travelList = travelListService.getTravelListByUser(userNumber);

        return ResponseEntity.ok(travelList);

    }
}
