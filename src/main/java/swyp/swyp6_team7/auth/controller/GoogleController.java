package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.dto.SignupRequestDto;
import swyp.swyp6_team7.auth.service.GoogleService;
import swyp.swyp6_team7.auth.service.KakaoService;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class GoogleController {

    private final GoogleService googleService;

    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    // 구글 로그인 리디렉션
    @GetMapping("/login/oauth/google")
    public ResponseEntity<Void> googleLoginRedirect(HttpSession session) {
        String state = UUID.randomUUID().toString(); // CSRF 방지용 state 값 생성
        session.setAttribute("oauth_state", state);  // 세션에 state 값 저장

        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId
                + "&response_type=code"
                + "&scope=email%20profile"  // 이메일과 프로필 정보 요청
                + "&redirect_uri=" + redirectUri
                + "&state=" + state;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(googleAuthUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    // google 인증 후, 리다이렉트된 URI에서 코드를 처리
    @GetMapping("/login/oauth/google/callback")
    public ResponseEntity<?> googleCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            HttpSession session) {

        // 세션에서 저장한 state 값 가져오기
        String sessionState = (String) session.getAttribute("oauth_state");
        if (sessionState != null && !sessionState.equals(state)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid state parameter");
        }

        // Google 인증 코드로 로그인 처리
        try {
            Map<String, String> userInfo = googleService.processGoogleLogin(code);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to process Google login: " + e.getMessage());
        }


    }
    @PutMapping("/api/social/google/complete-signup")
    public ResponseEntity<Map<String, String>> completeGoogleSignup(@RequestBody SignupRequestDto signupData) {
        // 클라이언트로부터 받은 추가 정보를 저장
        Map<String, String> result = googleService.completeSignup(signupData);
        return ResponseEntity.ok(result);
    }
}
