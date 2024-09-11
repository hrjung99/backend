package swyp.swyp6_team7.auth.service;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.dto.LoginRequestDto;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;

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

    public String login(LoginRequestDto loginRequestDto) {
        Users user = userRepository.findByUserEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 이메일을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getUserPw())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getUserSocialTF()) { //소셜 로그인으로 가입된 사용자일 경우 예외 처리
            throw new IllegalArgumentException("간편 로그인으로 가입된 계정입니다. 소셜 로그인으로 접속해 주세요.");
        }

        // 로그인 성공 시 JWT 토큰 발급
        return jwtProvider.createToken(user.getUserEmail(), user.getRoles());
    }
}
