package swyp.swyp6_team7.auth.service;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swyp.swyp6_team7.auth.provider.NaverProvider;
import swyp.swyp6_team7.member.entity.*;
import swyp.swyp6_team7.member.repository.SocialUserRepository;
import swyp.swyp6_team7.member.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class NaverService {

    private final NaverProvider naverProvider;
    private final UserRepository userRepository;
    private final SocialUserRepository socialUserRepository;
    @Autowired
    public NaverService(NaverProvider naverProvider, UserRepository userRepository, SocialUserRepository socialUserRepository) {
        this.naverProvider = naverProvider;
        this.userRepository = userRepository;
        this.socialUserRepository = socialUserRepository;
    }
    // 네이버 로그인 URL 생성
    public String naverLogin() {
        String state = UUID.randomUUID().toString(); // 무작위 state 값 생성
        String naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
                + naverProvider.getClientId() + "&redirect_uri=" + naverProvider.getRedirectUri() + "&state=" + state;

        return naverAuthUrl;
    }

    public Map<String, String> getUserInfoFromNaver(String code, String state) {
        // NaverProvider에서 사용자 정보 가져오기
        return naverProvider.getUserInfoFromNaver(code, state);
    }
    public Map<String, String> processNaverLogin(String code, String state) {
        // 1. NaverProvider에서 사용자 정보 가져오기
        Map<String, String> userInfo = naverProvider.getUserInfoFromNaver(code, state);

        // 2. 사용자 정보가 성공적으로 받아졌다면 DB에 저장
        saveSocialUser(
                userInfo.get("email"),
                userInfo.get("name"),
                userInfo.get("gender"),
                userInfo.get("socialID"),  // 네이버의 고유 소셜 ID
                userInfo.get("ageGroup"),
                userInfo.get("provider")
        );

        // 3. 필요한 추가 로직 처리 후 반환 (예: 토큰 생성, 사용자 세션 관리 등)
        return userInfo;
    }

    // Users와 SocialUsers에 저장하는 메서드
    @Transactional
    private void saveSocialUser(String email, String name, String gender, String socialLoginId, String ageGroup, String provider) {
        // 로그 추가
        System.out.println("Saving user: " + email);

        // Users 테이블에서 해당 이메일로 사용자 찾기
        Optional<Users> existingUser = userRepository.findByUserEmail(email);

        Users user;
        if (existingUser.isPresent()) {
            user = existingUser.get();  // 기존 사용자 정보
        } else {
            // 사용자 정보가 없으면 새로 생성
            user = new Users();
            user.setUserEmail(email);
            user.setUserName(name);
            user.setUserPw("social-login");
            if (gender != null && !gender.isEmpty()) {
                user.setUserGender(Gender.valueOf(gender));
            }  // 성별 변환 처리
            user.setUserAgeGroup(AgeGroup.fromValue(ageGroup));
            user.setUserSocialTF(true);  // 소셜 로그인 여부 true
            user.setUserStatus(UserStatus.ABLE);
            user.setRole(UserRole.USER);
            userRepository.save(user);
            System.out.println("User saved: " + user.getUserEmail());
        }

        // SocialUsers 테이블에 소셜 정보 저장
        if (!socialUserRepository.existsBySocialLoginId(socialLoginId)) {
            SocialUsers socialUser = new SocialUsers();
            socialUser.setUser(user);
            socialUser.setSocialLoginId(socialLoginId);
            socialUser.setSocialEmail(email);
            socialUser.setSocialProvider(SocialProvider.fromString(provider));
            socialUserRepository.save(socialUser);
            System.out.println("Social user saved: " + socialLoginId);
        }

    }
}
