package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.auth.service.SocialLoginService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
public class SocialLoginController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final SocialLoginService socialLoginService;

    public SocialLoginController(JwtProvider jwtProvider, UserRepository userRepository, SocialLoginService socialLoginService) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.socialLoginService = socialLoginService;
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleSocialLogin(String provider,
                                                                 String code,
                                                                 String state,
                                                                 HttpServletResponse response) {
        // 소셜 로그인 처리 후 사용자 정보 가져오기
        Users user = socialLoginService.handleSocialLogin(provider, code, state);

        // JWT 토큰 생성
        String accessToken = jwtProvider.createAccessToken(
                user.getUserEmail(),
                user.getUserNumber(),
                List.of(user.getRole().name()));
        String refreshToken = jwtProvider.createRefreshToken(user.getUserEmail(), user.getUserNumber());

        // 리프레시 토큰을 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 전송
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 1주일
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        // 액세스 토큰을 JSON 응답으로 반환
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("accessToken", accessToken);
        responseMap.put("userId", String.valueOf(user.getUserNumber()));

        return ResponseEntity.ok(responseMap);
    }

}
