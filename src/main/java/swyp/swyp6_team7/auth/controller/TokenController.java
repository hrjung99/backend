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
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public TokenController(JwtProvider jwtProvider,UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    // Refresh Token으로 새로운 Access Token 발급
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookies(request);

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh Token이 존재하지 않습니다.");
        }

        try {
            // 유효한 Refresh Token인지 검증
            if (jwtProvider.validateToken(refreshToken)) {
                // Refresh Token에서 이메일 정보 추출
                String userEmail = jwtProvider.getUserEmail(refreshToken);
                Users user = userRepository.findByUserEmail(userEmail)
                        .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다. 이메일: " + userEmail));

                // 사용자 권한 리스트 추출
                List<String> roles = user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                // 새로운 Access Token 발급
                String newAccessToken = jwtProvider.createAccessToken(user.getUserEmail(), user.getUserNumber(), roles);

                // 새로운 Access Token을 항상 응답으로 반환
                return ResponseEntity.ok("Bearer " + newAccessToken);
            } else {
                return ResponseEntity.status(403).body("Refresh Token이 유효하지 않습니다.");
            }
        } catch (JwtException e) {
            return ResponseEntity.status(403).body("Refresh Token이 유효하지 않습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body("서버 에러: " + e.getMessage());
        }
    }

    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
