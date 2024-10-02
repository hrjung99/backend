package swyp.swyp6_team7.travel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<TravelListResponseDto>> getMyCreatedTravels(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        // JWT 토큰에서 사용자 ID 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 여행 목록 조회
        Page<TravelListResponseDto> travelList = travelListService.getTravelListByUser(userNumber, pageable);

        return ResponseEntity.ok(travelList);

    }
}
