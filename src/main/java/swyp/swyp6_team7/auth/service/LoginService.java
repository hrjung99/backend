package swyp.swyp6_team7.auth.service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public  Map<String, String> login(LoginRequestDto loginRequestDto,HttpServletResponse response) {
        Users user = userRepository.findByUserEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getUserSocialTF()) { //소셜 로그인으로 가입된 사용자일 경우 예외 처리
            throw new IllegalArgumentException("간편 로그인으로 가입된 계정입니다. 소셜 로그인으로 접속해 주세요.");
        }

        // Access Token 생성
        String accessToken = jwtProvider.createAccessToken(user.getUserEmail(),user.getUserNumber(), List.of(user.getRole().name()));

        // Refresh Token 생성
        String refreshToken = jwtProvider.createRefreshToken(user.getUserEmail(),user.getUserNumber());

        // RefreshToken 쿠키 삭제
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);

        //새 RefreshToken 쿠키 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 1주일
        response.addCookie(refreshTokenCookie);

        // Access Token을 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        return tokens;
    }
    // 이메일로 유저를 조회하는 메서드 추가
    public Users getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
