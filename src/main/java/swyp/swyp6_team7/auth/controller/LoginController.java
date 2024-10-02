package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.service.LoginService;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.service.MemberService;
import swyp.swyp6_team7.member.service.UserLoginHistoryService;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/api")
public class LoginController {
    private final LoginService loginService;
    private final UserLoginHistoryService userLoginHistoryService;
    private final MemberService memberService;

    public LoginController(LoginService loginService, UserLoginHistoryService userLoginHistoryService, MemberService memberService) {
        this.loginService = loginService;
        this.userLoginHistoryService = userLoginHistoryService;
        this.memberService = memberService;
    }


    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            // LoginService에서 실제로 로그인 유저를 가져오도록 수정
            Map<String, String> tokenMap = loginService.login(loginRequestDto, response);
            String accessToken = tokenMap.get("accessToken");

            Users user = loginService.getUserByEmail(loginRequestDto.getEmail()); // 로그인한 유저 정보 가져오기
            userLoginHistoryService.saveLoginHistory(user);  // 로그인 이력 저장
            memberService.updateLoginDate(user);  // 로그인 시간 업데이트
            Map<String, String> tokens = loginService.login(loginRequestDto, response);
            return ResponseEntity.ok(tokens); // Access Token 반환
        } catch (UsernameNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);  // 404 Not Found 반환
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);  // 400 Bad Request 반환
        }
    }


}
