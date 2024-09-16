package swyp.swyp6_team7.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;
import swyp.swyp6_team7.member.entity.SocialUsers;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.SocialUserRepository;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SocialLoginService {
    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SocialLoginService(UserRepository userRepository, SocialUserRepository socialUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.socialUserRepository = socialUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users handleSocialLogin(SocialUserDTO socialUserDTO) {
        // 소셜 이메일로 Users 테이블에서 사용자 찾기
        Optional<Users> existingUser = userRepository.findByUserEmail(socialUserDTO.getEmail());

        if (existingUser.isPresent()) {
            // 이미 가입된 유저가 있으면 로그인 처리
            return existingUser.get();
        } else {
            // 가입된 유저가 없으면 새로운 사용자 생성
            Users newUser = new Users();
            newUser.setUserEmail(socialUserDTO.getEmail());
            newUser.setUserName(socialUserDTO.getName());
            newUser.setUserGender(Users.Gender.valueOf(socialUserDTO.getGender().toUpperCase()));
            newUser.setUserBirthYear(socialUserDTO.getBirthYear());
            newUser.setUserPhone(socialUserDTO.getPhoneNumber());

            // 소셜 로그인일 경우 비밀번호는 임의값으로 저장 (암호화)
            newUser.setUserPw(passwordEncoder.encode("social-login"));

            // 기본 상태값 설정
            newUser.setUserStatus(Users.MemberStatus.ABLE);
            newUser.setUserRole("user");
            newUser.setUserSocialTF(true);
            newUser.setUserRegDate(LocalDateTime.now());

            // Users 테이블에 저장
            Users savedUser = userRepository.save(newUser);

            // Social_Users 테이블에 소셜 로그인 정보 저장
            SocialUsers socialUser = new SocialUsers();
            socialUser.setUser(savedUser);
            socialUser.setSocialEmail(socialUserDTO.getEmail());
            socialUser.setSocialProvider(socialUserDTO.getProvider());

            socialUserRepository.save(socialUser);

            return savedUser;
        }
    }
}

