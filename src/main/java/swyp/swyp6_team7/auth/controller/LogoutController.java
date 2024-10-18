package swyp.swyp6_team7.auth.controller;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.auth.service.CustomUserDetails;
import swyp.swyp6_team7.auth.service.JwtBlacklistService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.member.service.UserLoginHistoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);
    @Autowired
    private final UserLoginHistoryService userLoginHistoryService;
    @Autowired
    private final MemberService memberService;
    @Autowired
    private final JwtBlacklistService jwtBlacklistService;
    @Autowired
    private final JwtProvider jwtProvider;

    public LogoutController(UserLoginHistoryService userLoginHistoryService, MemberService memberService,
                            JwtBlacklistService jwtBlacklistService,JwtProvider jwtProvider) {
        this.userLoginHistoryService = userLoginHistoryService;
        this.memberService = memberService;
        this.jwtBlacklistService = jwtBlacklistService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 인증된 유저 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info("Authentication principal: {}", authentication.getPrincipal());
            if (authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                logger.info("Authenticated user's email: {}", customUserDetails.getUsername());

                // 이메일로 Users 엔티티를 찾음
                Users user = memberService.getUserByEmail(customUserDetails.getUsername());

                // 로그아웃 이력 업데이트
                userLoginHistoryService.updateLogoutHistory(user);
                memberService.updateLogoutDate(user);

                // Access Token 추출 (Authorization 헤더에서 추출)
                String authorizationHeader = request.getHeader("Authorization");
                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    String accessToken = authorizationHeader.substring(7);  // "Bearer " 이후 토큰 부분 추출

                    // Access Token이 유효한지 검증
                    if (jwtProvider.validateToken(accessToken)) {
                        // 토큰의 만료 시간을 추출
                        long expirationTime = jwtProvider.getExpiration(accessToken);

                        // 블랙리스트에 토큰 추가
                        jwtBlacklistService.addToBlacklist(accessToken, expirationTime);

                        logger.info("Access Token added to blacklist: {}", accessToken);
                    } else {
                        logger.warn("Invalid or expired Access Token");
                    }
                } else {
                    logger.warn("Authorization header is missing or invalid");
                }

                // 클라이언트 측의 refreshToken 쿠키 삭제
                Cookie deleteCookie = new Cookie("refreshToken", null);
                deleteCookie.setMaxAge(0);
                deleteCookie.setPath("/");
                deleteCookie.setHttpOnly(true);
                response.addCookie(deleteCookie);

                // SecurityContext에서 인증 정보 제거
                SecurityContextHolder.clearContext();

                return ResponseEntity.ok("Logout successful");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user is logged in");

    }
}
