package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class RefreshController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public RefreshController(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        // 쿠키에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();

                    if (jwtProvider.validateToken(refreshToken)) {
                        String userEmail = jwtProvider.getUserEmail(refreshToken);
                        Users user = userRepository.findByUserEmail(userEmail)
                                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

                        // 새로운 Access Token 발급
                        String newAccessToken = jwtProvider.createAccessToken(user.getUserEmail(), user.getRoles());
                        return ResponseEntity.ok("Bearer " + newAccessToken);
                    }
                }
            }
        }
        return ResponseEntity.status(403).body("Invalid Refresh Token");
    }
}
