package swyp.swyp6_team7.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.service.LoginService;

@RestController
//@RequestMapping("/api")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            String accessToken = loginService.login(loginRequestDto, response); // response 추가
            return ResponseEntity.ok("Bearer " + accessToken); // Access Token 반환
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // 404 Not Found 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  // 400 Bad Request 반환
        }
    }
}
