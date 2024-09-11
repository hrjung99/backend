package swyp.swyp6_team7.member.service;

import io.jsonwebtoken.Jwt;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import swyp.swyp6_team7.auth.jwt.JwtProvider;
import swyp.swyp6_team7.member.dto.UserRequestDto;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public MemberService(UserRepository userRepository, PasswordEncoder passwordEncoder,@Lazy JwtProvider jwtProvider){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }
    public Map<String, Object> signUp(UserRequestDto userRequestDto) {
        // 이메일 중복 확인
        if (userRepository.findByUserEmail(userRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        // Argon2로 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        // Users 객체에 암호화된 비밀번호 설정
        Users newUser =  Users.builder()
                .userEmail(userRequestDto.getEmail())
                .userPw(encodedPassword)  // 암호화된 비밀번호 설정
                .userFirstName(userRequestDto.getFirstName())
                .userLastName(userRequestDto.getLastName())
                .userPhone(userRequestDto.getPhone())
                .userGender(userRequestDto.getGender())
                .userBirthYear(userRequestDto.getBirthYear())
                .roles(List.of("ROLE_USER"))  // 기본 역할 설정
                .userStatus("active")  // 기본 사용자 상태 설정
                .build();
        newUser.setPassword(encodedPassword);

        // 사용자 저장
        userRepository.save(newUser);

        // JWT 발급
        String token = jwtProvider.createToken(newUser.getEmail(), newUser.getRoles());

        // 응답 데이터에 userId와 accessToken 포함
        Map<String, Object> response = new HashMap<>();
        response.put("userNumber", newUser.getUserNumber());
        response.put("email", newUser.getUserEmail());
        response.put("accessToken", token);

        return response;
    }
}
