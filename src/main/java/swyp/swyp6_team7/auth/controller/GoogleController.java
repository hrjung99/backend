package swyp.swyp6_team7.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.service.GoogleService;

import java.util.Map;

@RestController
public class GoogleController {
    private final GoogleService googleService;

    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    // 구글 OAuth 2.0 인증 후 callback 처리
    @GetMapping("/login/oauth/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        String accessToken = googleService.getAccessToken(code);
        Map<String, Object> userInfo = googleService.getUserInfo(accessToken);

        return ResponseEntity.ok(userInfo);
    }
}
