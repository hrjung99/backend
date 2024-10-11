package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.auth.service.SocialLoginService;
import swyp.swyp6_team7.member.entity.SocialUsers;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.SocialUserRepository;
import swyp.swyp6_team7.member.repository.UserRepository;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.member.service.UserLoginHistoryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/social")
public class SocialLoginController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final SocialLoginService socialLoginService;
    private final UserLoginHistoryService userLoginHistoryService;
    private final MemberService memberService;

    public SocialLoginController(JwtProvider jwtProvider,
                                 UserRepository userRepository,
                                 SocialLoginService socialLoginService,
                                 UserLoginHistoryService userLoginHistoryService,
                                 MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.socialLoginService = socialLoginService;
        this.userLoginHistoryService = userLoginHistoryService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleSocialLogin(@RequestBody Map<String, String> loginRequest,
                                                                 HttpServletResponse response) {
        String socialLoginId = loginRequest.get("socialNumber");
        String email = loginRequest.get("email");

        Users user = socialLoginService.handleSocialLogin(socialLoginId, email);

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

        // 로그인 이력 저장
        userLoginHistoryService.saveLoginHistory(user);
        memberService.updateLoginDate(user);  // 로그인 시간 업데이트

        // 액세스 토큰을 JSON 응답으로 반환
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("accessToken", accessToken);
        responseMap.put("userId", String.valueOf(user.getUserNumber()));

        return ResponseEntity.ok(responseMap);
    }

}
