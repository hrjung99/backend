package swyp.swyp6_team7.travel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.travel.dto.response.TravelListResponseDto;
import swyp.swyp6_team7.travel.service.TravelAppliedService;

import java.util.List;

@RestController
@RequestMapping("/api/my-applied-travels")
@RequiredArgsConstructor
public class TravelAppliedController {

    private final TravelAppliedService travelAppliedService;
    private final JwtProvider jwtProvider;

    // 사용자가 신청한 여행 목록 조회
    @GetMapping("")
    public ResponseEntity<Page<TravelListResponseDto>> getAppliedTrips(
            @RequestHeader("Authorization") String token,
            Pageable pageable
    ) {
        // JWT 토큰에서 사용자 ID 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 여행 목록 조회
        Page<TravelListResponseDto> appliedTrips = travelAppliedService.getAppliedTripsByUser(userNumber, pageable);

        return ResponseEntity.ok(appliedTrips);
    }

    // 사용자가 특정 여행에 대한 참가 취소
    @DeleteMapping("/{travelNumber}/cancel")
    public ResponseEntity<Void> cancelTripApplication(@RequestHeader("Authorization") String token, @PathVariable("travelNumber") int travelNumber) {
        // JWT 토큰에서 사용자 ID 추출
        String jwtToken = token.replace("Bearer ", "");
        Integer userNumber = jwtProvider.getUserNumber(jwtToken);

        // 참가 취소 처리
        travelAppliedService.cancelApplication(userNumber, travelNumber);
        return ResponseEntity.noContent().build();
    }
}
