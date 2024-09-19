package swyp.swyp6_team7.auth.controller;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.repository.UserRepository;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtProvider jwtProvider;

    public TokenController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    // Refresh Token으로 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request) {
        // 쿠키에서 Refresh Token을 추출
        String refreshToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh Token이 존재하지 않습니다.");
        }

        try {
            // 유효한 Refresh Token으로 새로운 Access Token 발급
            String newAccessToken = jwtProvider.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } catch (JwtException e) {
            return ResponseEntity.status(403).body("Refresh Token이 유효하지 않습니다.");
        }
    }
}
