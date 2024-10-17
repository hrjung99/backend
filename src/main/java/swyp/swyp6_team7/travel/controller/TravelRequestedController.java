package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.service.TravelRequestedService;

@RestController
@RequestMapping("/api/my-requested-travels")
@RequiredArgsConstructor
public class TravelRequestedController {
    private final TravelRequestedService travelRequestedService;
    private final JwtProvider jwtProvider;

    //신청한 여행 목록 조회
    @GetMapping("")
    public ResponseEntity<Page<TravelListResponseDto>> getRequestedTrips(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        // JWT 토큰에서 사용자 번호 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 서비스 호출하여 신청한 여행 목록 조회
        Page<TravelListResponseDto> travelList = travelRequestedService.getRequestedTripsByUser(userNumber, pageable);

        return ResponseEntity.ok(travelList);
    }
    // 참가 취소
    @DeleteMapping("/{travelNumber}/cancel")
    public ResponseEntity<Void> cancelTripApplication(@RequestHeader("Authorization") String token, @PathVariable("travelNumber") int travelNumber) {
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 참가 취소 처리
        travelRequestedService.cancelApplication(userNumber, travelNumber);
        return ResponseEntity.noContent().build();
    }
}
