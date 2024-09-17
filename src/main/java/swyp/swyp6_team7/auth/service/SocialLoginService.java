package swyp.swyp6_team7.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import swyp.swyp6_team7.auth.dto.SocialUserDTO;
import swyp.swyp6_team7.auth.provider.SocialLoginProvider;
import swyp.swyp6_team7.member.entity.SocialUsers;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.member.repository.SocialUserRepository;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.security.Security.getProvider;

@Service
public class SocialLoginService {
    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    private final List<SocialLoginProvider> socialLoginProviders;

    public SocialLoginService(UserRepository userRepository, SocialUserRepository socialUserRepository,
                              List<SocialLoginProvider> socialLoginProviders) {
        this.userRepository = userRepository;
        this.socialUserRepository = socialUserRepository;
        this.socialLoginProviders = socialLoginProviders;
    }

    @Transactional
    public Users handleSocialLogin(String provider, String code, String state) {
        // 소셜 제공자별로 분기 처리
        SocialLoginProvider socialLoginProvider = getProvider(provider);
        SocialUserDTO socialUserDTO = socialLoginProvider.getUserInfo(code, state);

        return processUser(socialUserDTO);
    }
    private SocialLoginProvider getProvider(String provider) {
        return socialLoginProviders.stream()
                .filter(p -> p.supports(provider))  // 각 제공자 클래스에서 provider를 지원하는지 체크
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported provider: " + provider));
    }

    private Users processUser(SocialUserDTO socialUserDTO){
    // 1. users 테이블에 저장 (중복 체크)
        Optional<Users> existingUserOpt = userRepository.findByUserEmail(socialUserDTO.getEmail());
        Users user;
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            // 새로운 유저 생성
            user = createUserFromDTO(socialUserDTO);
            user = userRepository.save(user);
        }

        // 2. social_users 테이블에 저장 (중복 체크 후 저장)
        Optional<SocialUsers> existingSocialUser = socialUserRepository.findBySocialNumber(Integer.parseInt(socialUserDTO.getSocialNumber()));
        if (!existingSocialUser.isPresent()) {
            SocialUsers socialUser = new SocialUsers();
            socialUser.setUser(user);
            socialUser.setSocialLoginId(socialUserDTO.getSocialNumber());// 소셜 ID 설정
            socialUser.setSocialProvider(socialUserDTO.getProvider());    // 소셜 제공자 설정
            socialUser.setSocialEmail(socialUserDTO.getEmail());          // 소셜 이메일 설정

            socialUserRepository.save(socialUser);
        }

        return user;
    }

    private Users createUserFromDTO(SocialUserDTO socialUserDTO) {
        Users newUser = new Users();
        newUser.setUserEmail(socialUserDTO.getEmail());
        newUser.setUserName(socialUserDTO.getName() != null ? socialUserDTO.getName() : "Unknown");
        newUser.setUserGender(socialUserDTO.getGender() != null ? Users.Gender.valueOf(socialUserDTO.getGender().toUpperCase()) : Users.Gender.F);
        newUser.setUserBirthYear(socialUserDTO.getBirthYear() != null ? socialUserDTO.getBirthYear() : "0000");
        newUser.setUserPhone(socialUserDTO.getPhoneNumber() != null ? socialUserDTO.getPhoneNumber() : "000-0000-0000");
        newUser.setUserPw("social-login"); // 소셜 로그인 유저는 패스워드 생성 불필요
        newUser.setUserStatus(Users.MemberStatus.ABLE);
        newUser.setUserRole("user");
        newUser.setUserSocialTF(true);
        newUser.setUserRegDate(LocalDateTime.now());
        return newUser;
    }
}

