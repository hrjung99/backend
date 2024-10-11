package swyp.swyp6_team7.auth.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.dto.SignupRequestDto;
import swyp.swyp6_team7.auth.service.KakaoService;
import swyp.swyp6_team7.member.entity.Users;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 카카오 로그인 리디렉션
    @GetMapping("/login/oauth/kakao")
    public ResponseEntity<Void> kakaoLoginRedirect(HttpSession session) {
        String state = UUID.randomUUID().toString(); // CSRF 방지용 state 값 생성
        session.setAttribute("oauth_state", state);  // 세션에 state 값 저장

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
                + "&response_type=code"
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(kakaoAuthUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // 카카오 인증 후, 리다이렉트된 URI에서 코드를 처리
    @GetMapping("/login/oauth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            HttpSession session) {

        // 세션에서 저장한 state 값 가져오기 (선택 사항)
        String sessionState = (String) session.getAttribute("oauth_state");
        if (sessionState != null && !sessionState.equals(state)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid state parameter");
        }

        // 카카오 인증 코드로 로그인 처리
        try {
            Map<String, String> userInfo = kakaoService.processKakaoLogin(code);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to process Kakao login: " + e.getMessage());
        }


    }
    @PutMapping("/api/social/kakao/complete-signup")
    public ResponseEntity<String> completeKakaoSignup(@RequestBody SignupRequestDto signupData) {
        // 클라이언트로부터 받은 추가 정보를 저장
        String result = kakaoService.completeSignup(signupData);
        return ResponseEntity.ok(result);
    }
}
